package com.devdd.recipe.utils.extensions

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.CompressFormat.*
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Base64
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.net.toFile
import androidx.core.net.toUri
import coil.Coil
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.devdd.recipe.constants.JPEG_EXT
import com.devdd.recipe.constants.RECIPE_EXTERNAL_STORAGE_ROOT
import com.devdd.recipe.constants.TEMP_DIR
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.io.*
import kotlin.coroutines.resume
import kotlin.math.roundToInt


fun Context.createFile(parent: String, fileName: String, ext: String): File {
    return File("${createFileDirectory(parent)}${File.separator}${fileName}.$ext")
}

fun Context.createFile(path: String): File {
    val file = File(path)
    val parent = file.parent
    if (parent != null) createFileDirectory(parent)
    return file
}

private fun Context.createFileDirectory(parent: String): String {
    val directory = File(filesDir, parent)
    directory.mkdirs()
    return directory.absolutePath
}

// Checks if a volume containing external storage is available
// for read and write.
fun isExternalStorageWritable(): Boolean {
    return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}

// Checks if a volume containing external storage is available to at least read.
fun isExternalStorageReadable(): Boolean {
    return Environment.getExternalStorageState() in
            setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
}

fun Context.createExternalImageFile(parent: String?, fileName: String, ext: String): File? {
    return if (isExternalStorageReadable() && isExternalStorageWritable()) {
        File("${createExternalImageFileDirectory(parent)}${File.separator}${fileName}.$ext")
    } else null
}

fun Context.createExternalImageFileDirectory(parent: String?): String {
    val root = if (parent.isNullOrBlank()) RECIPE_EXTERNAL_STORAGE_ROOT
    else "$RECIPE_EXTERNAL_STORAGE_ROOT${File.separator}$parent"

    val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val directory = File(dir, root)
    directory.mkdirs()
    return directory.absolutePath
}

private const val SCHEME_FILE = "file"

fun requireValidFile(fileUri: Uri?, checkForExistence: Boolean): File {
    val file = validateFilePath(fileUri, checkForExistence)
    return requireNotNull(file, { "Invalid File doesn't exists" })
}

fun requireValidFile(path: String?, checkForExistence: Boolean): File {
    val file = validateFilePath(path, checkForExistence)
    return requireNotNull(file, { "Invalid File doesn't exists" })
}

fun validateFilePath(path: String?, checkForExistence: Boolean): File? {
    if (path.isNullOrBlank()) return null

    val fileUri = when (path.toUri().scheme) {
        "file" -> path.toUri()
        else -> path.toFileUri()
    }

    return validateFilePath(fileUri, checkForExistence)
}

fun validateFilePath(fileUri: Uri?, checkForExistence: Boolean): File? {
    fileUri ?: return null
    val file = if (fileUri.scheme == SCHEME_FILE) fileUri.toFile() else null
    return when {
        checkForExistence.not() -> file
        file?.exists() == true -> file
        else -> null
    }
}

suspend fun File.copyFrom(
    context: Context? = null,
    uri: Uri? = null,
    fileStream: InputStream? = null,
): File {
    return suspendCancellableCoroutine { cont ->
        try {
            val sourceInputStream = fileStream ?: if (uri != null) {
                when {
                    SCHEME_FILE == uri.scheme -> FileInputStream(uri.path)
                    context != null -> context.contentResolver.openInputStream(uri)
                    else -> throw IllegalArgumentException("For non-file scheme uri, context is required")
                }
            } else throw IllegalArgumentException("At least one argument is required not-null")

            sourceInputStream?.use { inputStream ->
                val sourceOutputStream = FileOutputStream(this@copyFrom)
                sourceOutputStream.use { outputStream ->
                    val buffer = ByteArray(4 * 1024) // buffer size
                    while (true) {
                        val byteCount = inputStream.read(buffer)
                        if (byteCount < 0) break
                        outputStream.write(buffer, 0, byteCount)
                    }
                    outputStream.flush()
                }
            }
            cont.resume(this)
        } catch (e: IOException) {
            cont.cancel(e)
        } catch (e: FileNotFoundException) {
            cont.cancel(e)
        } catch (e: IllegalArgumentException) {
            cont.cancel(e)
        }
    }
}

fun Bitmap.saveToImageDirectory(
    imageFile: File,
    compressFormat: Bitmap.CompressFormat = defaultCompressFormat,
): Boolean {
    try {
        FileOutputStream(imageFile).use { oStream ->
            compress(compressFormat, 100, oStream)
            oStream.flush()
        }
    } catch (e: Exception) {
        imageFile.delete()
        e.printStackTrace()
        Timber.e("There was an issue saving the image ${imageFile.name}.")
        return false
    }
    return true
}

fun View.drawBitmap(): Bitmap {
    // Measure Specs (Unspecified)
    if (isAttachedToWindow.not()) {
        measure(
            View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED)
        )
    }

    // Create bitmap
    val bitmap: Bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
    // Draw Bitmap on Canvas
    val canvas = Canvas(bitmap)
    layout(0, 0, measuredWidth, measuredHeight)
    val backgroundDrawable: Drawable? = background
    if (backgroundDrawable != null) {
        //has background drawable, then draw it on the canvas
        backgroundDrawable.draw(canvas)
    } else {
        //does not have background drawable, then draw white background on the canvas
        canvas.drawColor(Color.WHITE)
    }
    draw(canvas)
    return bitmap
}

fun File.toCompressedImageByteArray(
    context: Context,
    quality: Int = 80,
    sampleSize: Int = 2,
): ByteArray? {
    var bytes: ByteArray? = null
    var bitmap: Bitmap? = null
    try {
        val options = BitmapFactory.Options()
        if (sampleSize > 1) options.inSampleSize = 2
        bitmap = BitmapFactory.decodeFile(path, options)
        val baos = ByteArrayOutputStream()
        bitmap.compress(JPEG, quality, baos)
        val compressedImageBytes = baos.toByteArray()

        // Create new compressed file
        val newFile =
            context.createFile("$parent${File.separator}$TEMP_DIR", name, JPEG_EXT)
        FileOutputStream(newFile).use { oStream ->
            oStream.write(compressedImageBytes)
            oStream.flush()
        }

        // Copy Exif Info
        val exifInfo = ExifInterface(path)
        val exifTag = exifInfo.getAttribute(ExifInterface.TAG_ORIENTATION)

        // Save Exif Info
        val newExifInfo = ExifInterface(newFile.path)
        newExifInfo.setAttribute(ExifInterface.TAG_ORIENTATION, exifTag)
        newExifInfo.saveAttributes()

        bytes = newFile.readBytes()

        // delete temp file
        newFile.deleteRecursively()

    } catch (error: Exception) {
        Timber.e(error)
    } finally {
        bitmap?.recycle()
        return bytes
    }
}

fun File.toBase64(context: Context, compressQuality: Int = 80, sampleSize: Int = 2): String? {
    return Base64.encodeToString(
        toCompressedImageByteArray(context, compressQuality, sampleSize)
            ?: return null, Base64.DEFAULT
    )
}

fun File.compressToBase64(
    context: Context,
    compressQuality: Int = 80
): String? {
    return Base64.encodeToString(
        context.compressImage(path, compressQuality), Base64.DEFAULT
    )
}

fun String.toFileUri(): Uri = Uri.Builder().authority("")
    .path(this)
    .scheme("file")
    .build()

fun File.copyToCompressedImage(providedFile: File, quality: Int = 80): Boolean {
    var bitmap: Bitmap? = null
    try {
        val options = BitmapFactory.Options()
        options.inSampleSize = 2
        bitmap = BitmapFactory.decodeFile(path, options)
        val baos = ByteArrayOutputStream()
        bitmap.compress(JPEG, quality, baos)
        val compressedImageBytes = baos.toByteArray()

        // Create new compressed file
        FileOutputStream(providedFile).use { oStream ->
            oStream.write(compressedImageBytes)
            oStream.flush()
        }

        // Copy Exif Info
        val exifInfo = ExifInterface(path)
        val exifTag = exifInfo.getAttribute(ExifInterface.TAG_ORIENTATION)

        // Save Exif Info
        val newExifInfo = ExifInterface(providedFile.path)
        newExifInfo.setAttribute(ExifInterface.TAG_ORIENTATION, exifTag)
        newExifInfo.saveAttributes()
        return true
    } catch (error: Exception) {
        Timber.e(error)
        return false
    } finally {
        bitmap?.recycle()
    }
}

fun Context.createBitmap(@DrawableRes drawableId: Int): Bitmap? {
    return try {
        BitmapFactory.decodeResource(resources, drawableId)
    } catch (e: Exception) {
        Timber.d(e)
        null
    }
}

fun File.copyFrom(
    context: Context,
    @DrawableRes drawableId: Int,
    compressFormat: Bitmap.CompressFormat = JPEG,
): Boolean {
    return context.createBitmap(drawableId)
        ?.saveToImageDirectory(imageFile = this, compressFormat = compressFormat)
        ?: false
}

suspend fun Context.executeImageRequest(
    data: Any?,
    placeholder: Drawable? = null,
    errorPlaceHolder: Drawable? = null,
    isRoundAsCircle: Boolean = false,
    enableCrossfade: Boolean = true,
    disableCache: Boolean = false,
    disableHardwareRendering: Boolean = true,
): Drawable? {
    val requestBuilder = ImageRequest.Builder(this)
        .data(data)
        .placeholder(placeholder)
        .error(errorPlaceHolder)
        .crossfade(enableCrossfade)
        .allowHardware(disableHardwareRendering.not())

    if (disableCache) {
        requestBuilder.diskCachePolicy(CachePolicy.DISABLED)
        requestBuilder.memoryCachePolicy(CachePolicy.DISABLED)
        requestBuilder.networkCachePolicy(CachePolicy.DISABLED)
    }

    if (isRoundAsCircle) requestBuilder.transformations(CircleCropTransformation())
    return Coil.imageLoader(this).execute(requestBuilder.build()).drawable
}

@Suppress("DEPRECATION")
val defaultCompressFormat: Bitmap.CompressFormat =
    if (isAtLeastVersion(Build.VERSION_CODES.R)) WEBP_LOSSLESS else WEBP

fun Context.compressImage(filePath: String, quality: Int = 80): ByteArray? {
    var bytes: ByteArray? = null
    var scaledBitmap: Bitmap? = null
    val options = BitmapFactory.Options()

    // by setting this field as true, the actual bitmap pixels are not loaded in the memory.
    // Just the bounds are loaded. If you try the use the bitmap here, you will get null.
    options.inJustDecodeBounds = true
    var bmp = BitmapFactory.decodeFile(filePath, options)

    var actualHeight = options.outHeight
    var actualWidth = options.outWidth

    //max Height and width values of the compressed image is taken as 512*288
    val maxHeight = 288f
    val maxWidth = 512f
    var imgRatio = (actualWidth / actualHeight).toFloat()
    val maxRatio = maxWidth / maxHeight

    //width and height values are set maintaining the aspect ratio of the image
    if (actualHeight > maxHeight || actualWidth > maxWidth) {
        when {
            imgRatio < maxRatio -> {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            }
            imgRatio > maxRatio -> {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            }
            else -> {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }
    }

    //setting inSampleSize value allows to load a scaled down version of the original image
    options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

    //inJustDecodeBounds set to false to load the actual bitmap
    options.inJustDecodeBounds = false

    //this options allow android to claim the bitmap memory if it runs low on memory
    options.inTempStorage = ByteArray(16 * 1024)
    try {
        //  load the bitmap from its path
        bmp = BitmapFactory.decodeFile(filePath, options)
    } catch (exception: OutOfMemoryError) {
        exception.printStackTrace()
    }
    try {
        scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
    } catch (exception: OutOfMemoryError) {
        Timber.e(exception)
    }
    val ratioX = actualWidth / options.outWidth.toFloat()
    val ratioY = actualHeight / options.outHeight.toFloat()
    val middleX = actualWidth / 2.0f
    val middleY = actualHeight / 2.0f
    val scaleMatrix = Matrix()
    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
    val canvas = Canvas(scaledBitmap!!)
    canvas.setMatrix(scaleMatrix)
    canvas.drawBitmap(
        bmp,
        middleX - bmp.width / 2,
        middleY - bmp.height / 2,
        Paint(Paint.FILTER_BITMAP_FLAG)
    )

    //check the rotation of the image and display it properly
    val exif: ExifInterface
    try {
        exif = ExifInterface(filePath)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION, 0
        )

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                matrix.postRotate(90F)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                matrix.postRotate(180F)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                matrix.postRotate(270F)
            }
        }
        scaledBitmap = Bitmap.createBitmap(
            scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true
        )
    } catch (e: IOException) {
        Timber.e(e)
    }
    val out: FileOutputStream?
    val file = createFile(File(filePath).parent!!, TEMP_DIR, JPEG_EXT)

    try {
        out = FileOutputStream(file)

        //  write the compressed bitmap at the destination specified by filename.
        scaledBitmap!!.compress(JPEG, quality, out)
        bytes = file.readBytes()

        //delete temp file
        file.delete()

    } catch (e: FileNotFoundException) {
        Timber.e(e)
    }
    return bytes
}


fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
        val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()
        inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
    }
    val totalPixels = (width * height).toFloat()
    val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
    while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
        inSampleSize++
    }
    return inSampleSize
}
