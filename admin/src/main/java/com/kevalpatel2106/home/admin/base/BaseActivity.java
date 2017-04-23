package com.kevalpatel2106.home.admin.base;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Keval Patel on 23/04/17.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public class BaseActivity extends AppCompatActivity {
    private CompositeSubscription mSubscription = new CompositeSubscription();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        //Bind butter knife
        ButterKnife.bind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return false;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Add the subscription to the {@link CompositeSubscription}.
     *
     * @param subscription {@link Subscription}
     */
    protected void addSubscription(Subscription subscription) {
        mSubscription.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
    }
}
