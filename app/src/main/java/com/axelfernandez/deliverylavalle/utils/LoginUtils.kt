package com.axelfernandez.deliverylavalle.utils

import android.content.Context
import android.content.SharedPreferences
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.User
import com.axelfernandez.deliverylavalle.models.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginUtils {
companion object{

    fun getUserFromSharedPreferences(context: Context):User{
        var sharedPreferences : SharedPreferences = context.getSharedPreferences("userSession", Context.MODE_PRIVATE)
        return User(
            email = sharedPreferences.getString(context.getString(R.string.email),null).toString(),
            givenName = sharedPreferences.getString(context.getString(R.string.given_name),null).toString(),
            familyName = sharedPreferences.getString(context.getString(R.string.family_name),null).toString(),
            clientId = sharedPreferences.getString(context.getString(R.string.client_id),null).toString(),
            photo = sharedPreferences.getString(context.getString(R.string.picture),null).toString(),
            phone = sharedPreferences.getString(context.getString(R.string.phone),null).toString(),
            username = sharedPreferences.getString(context.getString(R.string.username),null).toString()
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
        editor.apply()
        

    }


}
}

