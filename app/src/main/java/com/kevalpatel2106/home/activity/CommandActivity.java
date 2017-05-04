package com.kevalpatel2106.home.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Toast;

import com.kevalpatel2106.home.R;
import com.kevalpatel2106.home.base.BaseActivity;
import com.kevalpatel2106.network.APIObserver;
import com.kevalpatel2106.network.RetrofitUtils;
import com.kevalpatel2106.network.requestPojo.SendCommandRequest;
import com.kevalpatel2106.network.responsePojo.PlainResponseData;

import butterknife.BindView;
import butterknife.OnClick;

public class CommandActivity extends BaseActivity {

    @BindView(R.id.command_et)
    AppCompatEditText mAppCompatEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);
    }

    @OnClick(R.id.send_fab)
    void sendCommand() {

        String commandStr = mAppCompatEditText.getText().toString().trim();
        if (!commandStr.isEmpty()) {
            SendCommandRequest request = new SendCommandRequest(this);
            request.setCommand(commandStr);
            addSubscription(RetrofitUtils.subscribe(RetrofitUtils.getApiService()
                            .sendCommand(RetrofitUtils.getAuthString(this), request),
                    new APIObserver<PlainResponseData>() {
                        @Override
                        public void onError(String errorMessage, int statusCode) {
                            Toast.makeText(CommandActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(PlainResponseData responseData) {
                            mAppCompatEditText.setText("");

                            Toast.makeText(CommandActivity.this,
                                    "Command sent successfully.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }));
        }
    }
}
