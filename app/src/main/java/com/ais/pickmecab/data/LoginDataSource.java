package com.ais.pickmecab.data;

import android.content.Context;


import com.ais.pickmecab.data.model.LoggedInUser;


import java.io.IOException;


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */



public class LoginDataSource {






    public Result<LoggedInUser> login(String username, String password, Context mContext) {




try{

            return new Result.Success<>(new LoggedInUser(
                    "userName",
                    "firstName"));

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

}
