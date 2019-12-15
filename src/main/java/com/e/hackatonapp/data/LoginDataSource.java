package com.e.hackatonapp.data;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.e.hackatonapp.R;
import com.e.hackatonapp.data.model.LoggedInUser;
import com.e.hackatonapp.ui.login.LoginActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private String m_Text = "";

    public static String executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public Result<LoggedInUser> login(String email, String password) {
        try {
            String fname,ftoken;

            String result=executePost("http://34.89.193.58:8080/getuser", "?email="+email+"&password="+password);
            Toast.makeText(LoginActivity.getAppContext(), result, Toast.LENGTH_LONG).show();
            if(result.equals("0")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.getAppContext());
                builder.setTitle("Hello! I see you are new, please input a name: ");

                final EditText input = new EditText(LoginActivity.getAppContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                if(!m_Text.equals("")) {
                    result = executePost("http://34.89.193.58:8080/user", "?name:" + m_Text + "&email=" + email + "&password=" + password);
                    if (result.equals("ok"))
                        Toast.makeText(LoginActivity.getAppContext(), "User Created! Welcome to our app!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(LoginActivity.getAppContext(), "Problem encountered! User not created. Try again please!", Toast.LENGTH_LONG).show();
                    fname = m_Text;
                } else throw new NullPointerException("No name received, try again!");
            } else {
                // process result
                JSONObject obj = new JSONObject(result);
                fname=obj.getString("name");
            }

            JSONObject obj = new JSONObject(result);
            ftoken = obj.getString("token");
            Toast.makeText(LoginActivity.getAppContext(), "We logged you in "+obj.getString("name"), Toast.LENGTH_LONG).show();
            LoggedInUser fakeUser = new LoggedInUser( ftoken, fname);
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            Toast.makeText(LoginActivity.getAppContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
