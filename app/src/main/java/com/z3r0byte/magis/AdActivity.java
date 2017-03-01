/*
 * Copyright 2016 Bas van den Boom 'Z3r0byte'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.z3r0byte.magis;

/*public class AdActivity extends AppCompatActivity {

    Toolbar mToolbar;
    Boolean ForceAction;
    Button doNothing;
    Button donate;
    Button wachtAd;
    IabHelper mHelper;

    static final String SKU_100_CENTS = "com.z3r0byte.magis.donation1";
    static final int RC_REQUEST = 10001;
    private static final String TAG = "AdActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ForceAction = extras.getParcelable("force");
        } else {
            ForceAction = false;
        }

        mToolbar = (Toolbar) findViewById(R.id.Toolbar);
        mToolbar.setTitle(R.string.title_ad);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        doNothing = (Button) findViewById(R.id.do_nothing);
        donate = (Button) findViewById(R.id.donate);
        wachtAd = (Button) findViewById(R.id.watch_ad);

        doNothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doNothing();
            }
        });
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donate();
            }
        });
        wachtAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watchAd();
            }
        });


        if (ForceAction) {
            startCountdown();
        } else {
            doNothing.setText(R.string.button_do_nothing_active);
            doNothing.setEnabled(true);
        }


        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmLr6TccuLpJerL0p0wvzn9l9rYNkVpu45TbeMwWBra2aLw30OePnxRjevs9tASRaq72Yv7pElQv2YMRPwjZKt0mnmkSDNQTCFTsadsq5FYsFZ8EK6S7DIsDIoe32rjST8+hqsMDuGaEtmuv7a/UKmWbzzMgaI+DQMx7qG9fimc2qmlup+hZzlqN9z4I4Wn5BzecIyAL0lLWV/fRsEQNeaFj3shqdfeZMZ0a14AbHwzNDLXdT4uvXLq2lrJieOFWZ2Y9c7hkkmkpA43VhCTo3NYSfhnDOrjbUM0PzDp7Fw4ol748qgKXJf8a/0ExEKM369U+xrE6bmTLFiStabUlsHwIDAQAB";
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
            }
        });

    }

    private void startCountdown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Boolean caughtException = false;
                for (int i = 20; i > 0; i--) {
                    try {
                        final int timer = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                doNothing.setText(String.format(getString(R.string.button_do_nothing), timer));
                            }
                        });
                        Thread.sleep(1000);
                    } catch (NullPointerException e) {
                        caughtException = true;
                        break;
                    } catch (InterruptedException e) {
                        caughtException = true;
                        break;
                    }
                }
                final Boolean Exception = caughtException;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!Exception) {
                            doNothing.setText(R.string.button_do_nothing_active);
                            doNothing.setEnabled(true);
                        }
                    }
                });
            }
        }).start();

    }


    public void donate() {
        mHelper.launchPurchaseFlow(this, SKU_100_CENTS, RC_REQUEST, mFinishedPurchaseListener, "some_key");
    }

    public void watchAd() {

    }

    public void doNothing() {
        if (ForceAction) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(this.getString(R.string.dialog_do_nothin_title));
            alertDialogBuilder.setMessage(this.getString(R.string.dialog_do_nothing_desc));
            alertDialogBuilder.setNegativeButton("Toch iets doen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alertDialogBuilder.setPositiveButton("Oké", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            finish();
        }
    }

    void complain(String message) {
        Log.e(TAG, "**** Magis Error: " + message);
    }

    IabHelper.OnIabPurchaseFinishedListener mFinishedPurchaseListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {
            if (result.isFailure()){
                //handle error
                return;
            } else if (info.getSku().equals(SKU_100_CENTS)){
                handlePurchase();
            }
        }
    };

    private void handlePurchase(){
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inv) {
            if (result.isFailure()){
                //handle error
                return;
            } else {
                mHelper.consumeAsync(inv.getPurchase(SKU_100_CENTS), mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (result.isSuccess()){
                sayThanks();
            } else {
                //handle error
            }
        }
    };

    private void sayThanks() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }
}
*/