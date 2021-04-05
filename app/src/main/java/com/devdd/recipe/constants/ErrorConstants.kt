@file:Suppress("SpellCheckingInspection")

package com.devdd.recipe.constants

import androidx.annotation.StringRes

private const val EINVALIDCONFIGDATA = 0x10000001
private const val EINTERNAL = 0x10000002
private const val EALREADYINUSE = 0x10000003
private const val EPERMDENIED = 0x10000004
private const val EUSEREXISTS = 0x10000005
private const val ENOTFOUND = 0x10000006
private const val ETOKENEXPIRED = 0x10000007
private const val EINVALIDOTP = 0x10000008
private const val EINVALIDPASSWD = 0x10000009
private const val EINSUFFICIENT_WALLET_BALANCE = 0x1000000A
private const val EACCOUNTLOCKED = 0x1000000B
private const val EMSGEXCEPTION = 0x1000000C
private const val EPROFILECLAIMED = 0x1000000D
private const val EUNCOMMITED_SESSION = 0x1000000E
private const val EPHONE_NUMBER_REGISTERED = 0x1000000F
private const val EDISABLED = 0x10000010
private const val EPAYMENTFAILED = 0x10000011
private const val ESTALEFCMTOKEN = 0x10000012
private const val EWEAKPASSWORD = 0x10000013
private const val EPHNUMBEROREMAILNOTVERIFIED = 0x10000014
private const val EACCESSCODE = 0x10000015
private const val EACCESSDENIED = 0x10000016
private const val EPAYMENTPERIODNOTSTARTED = 0x10000017
private const val EPAYMENTCYCLENOTSTARTED = 0x10000018
private const val EINVALIDLOCATION = 0x10000019
private const val EINVALIDKEY = 0x1000001A
private const val EACTIVEFOLLOWUP = 0x1000001B
private const val EACTIVECONSULTATION = 0x1000001C
private const val REFCODEOVERFLOWN = 0x1000001D
private const val EMULTIPLEAGENTSESSION = 0x1000001E
private const val INVALIDDATA = 0x1000001F
private const val EINVALIDPHONENUMBER = 0x10000020
private const val EINVALIDEMAILID = 0x10000021
private const val EINVALIDWORKPLACETYPE = 0x10000022
private const val EREFCODE = 0x10000023
private const val ELOCATION = 0x10000024
private const val EINSUFFICIENTBALANCEFORAAYUCARD = 0x10000025
private const val EPLANNOTFOUND = 0x10000026
private const val EACTIVESUBSCRIPTION = 0x10000027
private const val EINVALIDMAT = 0x10000028


private val errorMsgMapping: HashMap<Int, Int> = hashMapOf<Int, @StringRes Int>().apply {
//    put(EINTERNAL, R.string.EINTERNAL)
}

fun getLocalizedErrorMessage(errorCode: Int): Int? {
    return errorMsgMapping[errorCode]
}