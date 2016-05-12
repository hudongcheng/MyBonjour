package com.pantum.m.mybonjour;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    //final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    private final NsdManager.ResolveListener mResolveListener = new NsdManager.ResolveListener() {
        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {

        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            Log.i("Pantum Bonjour", "Host = " + serviceInfo.getHost() + ", port = " + serviceInfo.getPort());
            //builder.setMessage("click " + serviceInfo.getHost());
            //builder.create().show();
        }
    };
    private final ArrayList<NsdServiceInfo> mNsdServiceInfos = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final NsdManager mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);

        NsdManager.DiscoveryListener mDiscoveryListener = new NsdManager.DiscoveryListener() {

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.i("Pantum Bonjour", "onStartDiscoveryFailed");
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.i("Pantum Bonjour", "onStopDiscoveryFailed");
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.i("Pantum Bonjour", "onDiscoveryStarted");
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i("Pantum Bonjour", "onDiscoveryStopped");
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.i("Pantum Bonjour", "onServiceFound");
                Log.i("Pantum Bonjour", "Service name " + serviceInfo.getServiceName());

                //mNsdServiceInfos.put(serviceInfo.getServiceName(), serviceInfo);
                mNsdServiceInfos.add(serviceInfo);

                //Log.i("Pantum Bonjour", "host = " + serviceInfo.getHost());
                //Log.i("Pantum Bonjour", "port = " + serviceInfo.getPort());
/*                mNsdManager.resolveService(serviceInfo, new NsdManager.ResolveListener() {
                    @Override
                    public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {

                    }

                    @Override
                    public void onServiceResolved(NsdServiceInfo serviceInfo) {
                        Log.i("Pantum Bonjour", "Host = " + serviceInfo.getHost());
                    }
                });*/
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Log.i("Pantum Bonjour", "onServiceLost");
            }
        };

        mNsdManager.discoverServices("_ipp._tcp", NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ServiceAdapter(this, mNsdServiceInfos));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mNsdManager.resolveService(mNsdServiceInfos.get((int)id), mResolveListener);
            }
        });
    }

    class ServiceAdapter extends BaseAdapter {
        private Context mContext;
        private List<NsdServiceInfo> mObjects;
        private final LayoutInflater mInflater;

        public ServiceAdapter(Context context, List<NsdServiceInfo> objects) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mObjects = objects;
        }

        @Override
        public int getCount() {
            return mObjects.size();
        }

        @Override
        public Object getItem(int position) {
            return mObjects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            TextView text;

            if (convertView == null) {
                view = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            try {
                text = (TextView) view;
            } catch (ClassCastException e) {
                Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
                throw new IllegalStateException(
                        "ArrayAdapter requires the resource ID to be a TextView", e);
            }

            NsdServiceInfo item = (NsdServiceInfo)getItem(position);
            text.setText(item.getServiceName());

            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
