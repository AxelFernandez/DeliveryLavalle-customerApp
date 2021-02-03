package com.axelfernandez.deliverylavalle.utils

import android.content.Context
import android.content.SharedPreferences
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.Address
import com.axelfernandez.deliverylavalle.models.User
import com.axelfernandez.deliverylavalle.models.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginUtils {
    companion object{

        fun saveTokenFirebase(token: String, context: Context){
            val editor :SharedPreferences.Editor = context.getSharedPreferences("userSession", Context.MODE_PRIVATE).edit()
            editor.putString("token_firebase", token).apply()
        }

        fun saveLoginReady(context: Context){
            val editor :SharedPreferences.Editor = context.getSharedPreferences("userSession", Context.MODE_PRIVATE).edit()
            editor.putBoolean(context.getString(R.string.is_login_ready), true).apply()
        }


        fun getUserFromSharedPreferences(context: Context):User{
            var sharedPreferences : SharedPreferences = context.getSharedPreferences("userSession", Context.MODE_PRIVATE)
            return User(
                email = sharedPreferences.getString(context.getString(R.string.email),null).toString(),
                givenName = sharedPreferences.getString(context.getString(R.string.given_name),null).toString(),
                familyName = sharedPreferences.getString(context.getString(R.string.family_name),null).toString(),
                clientId = sharedPreferences.getString(context.getString(R.string.client_id),null).toString(),
                photo = sharedPreferences.getString(context.getString(R.string.picture),null).toString(),
                phone = sharedPreferences.getString(context.getString(R.string.phone),null).toString(),
                username = sharedPreferences.getString(context.getString(R.string.username),null).toString(),
                token = sharedPreferences.getString(context.getString(R.string.token),null).toString()

            )
        }
        fun putUserToSharedPreferences(context: Context, new_user: User){
            var editor :SharedPreferences.Editor = context.getSharedPreferences("userSession", Context.MODE_PRIVATE).edit()
            editor.putString("email",new_user.email)
            editor.putString("given_name",new_user.givenName)
            editor.putString("family_name",new_user.familyName)
            editor.putString("client_id",new_user.clientId)
            editor.putString("picture",new_user.photo)
            editor.putString("phone",new_user.phone)
            editor.putString("username",new_user.username)
            editor.putString("token",new_user.token)
            editor.apply()


        }
        fun saveSessionToken(token:String, context: Context){
            val editor :SharedPreferences.Editor = context.getSharedPreferences("userSession", Context.MODE_PRIVATE).edit()
            editor.putString("token", token).apply()
        }

        fun saveDefaultAddress(context: Context,address:Address){
            var editor :SharedPreferences.Editor = context.getSharedPreferences("userSession", Context.MODE_PRIVATE).edit()
            editor.putString("street",address.street)
            editor.putString("number",address.number)
            editor.putString("district",address.district)
            editor.putString("floor",address.floor)
            editor.putString("location",address.location)
            editor.putString("reference",address.reference)
            editor.putString("id",address.id)
            editor.apply()
        }
        fun getDefaultAddress(context: Context):Address{
            var sharedPreferences : SharedPreferences = context.getSharedPreferences("userSession", Context.MODE_PRIVATE)
            return Address(
                street = sharedPreferences.getString("street","No hay calle seleccionada").toString(),
                number = sharedPreferences.getString("number","").toString(),
                district = sharedPreferences.getString("district","").toString(),
                floor = sharedPreferences.getString("floor","").toString(),
                location = sharedPreferences.getString("location","").toString(),
                reference = sharedPreferences.getString("reference","").toString(),
                id = sharedPreferences.getString("id","").toString()

            )
        }
        fun removeAddress(context: Context){
            var editor :SharedPreferences.Editor = context.getSharedPreferences("userSession", Context.MODE_PRIVATE).edit()
            editor.remove("street")
            editor.remove("number")
            editor.remove("district")
            editor.remove("floor")
            editor.remove("location")
            editor.remove("reference")
            editor.remove("id")
            editor.apply()

        }
        fun removeUserData(context: Context){
            var editor :SharedPreferences.Editor = context.getSharedPreferences("userSession", Context.MODE_PRIVATE).edit()
            editor.clear().apply()

        }
    }
}

