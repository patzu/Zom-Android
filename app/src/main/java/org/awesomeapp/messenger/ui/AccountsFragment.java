package org.awesomeapp.messenger.ui;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.awesomeapp.messenger.ImApp;
import org.awesomeapp.messenger.provider.Imps;
import org.awesomeapp.messenger.service.IImConnection;
import org.awesomeapp.messenger.ui.legacy.SignInHelper;

import im.zom.messenger.R;

public class AccountsFragment extends ListFragment {

        private FragmentActivity mActivity;
        private int mAccountLayoutView;

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            mActivity = (FragmentActivity)activity;

            mAccountLayoutView = R.layout.account_view;

            initProviderCursor();


        }

        @Override
        public void onDetach() {
            super.onDetach();
            mActivity = null;
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
        }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        // Get the list
        ListView list = (ListView)v;

        // Get the list item position
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        int position = info.position;

        // Now you can do whatever.. (Example, load different menus for different items)
        //list.getItem(position);

        menu.add("One");
        menu.add("Two");
        menu.add("Three");

    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {


            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onViewCreated(view, savedInstanceState);
        }

        private void initProviderCursor()
        {
            final Uri uri = Imps.Provider.CONTENT_URI_WITH_ACCOUNT;

            mAdapter = new AccountAdapter(mActivity, new ProviderListItemFactory(), mAccountLayoutView);
            setListAdapter(mAdapter);

            mActivity.getSupportLoaderManager().initLoader(ACCOUNT_LOADER_ID, null, new LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    CursorLoader loader = new CursorLoader(mActivity, uri, PROVIDER_PROJECTION,
                            Imps.Provider.CATEGORY + "=?" + " AND " + Imps.Provider.ACTIVE_ACCOUNT_USERNAME + " NOT NULL" /* selection */,
                            new String[] { ImApp.IMPS_CATEGORY } /* selection args */,
                            Imps.Provider.DEFAULT_SORT_ORDER);
                    loader.setUpdateThrottle(100l);

                    return loader;
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
                    mAdapter.swapCursor(newCursor);
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    mAdapter.swapCursor(null);

                }
            });

        }


        private class ProviderListItemFactory implements LayoutInflater.Factory {
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                if (name != null && name.equals(ProviderListItem.class.getName())) {
                    return new ProviderListItem(context,attrs);
                }
                return null;
            }
        }



        AccountAdapter mAdapter;

        private static final String[] PROVIDER_PROJECTION = {
                                                             Imps.Provider._ID,
                                                             Imps.Provider.NAME,
                                                             Imps.Provider.FULLNAME,
                                                             Imps.Provider.CATEGORY,
                                                             Imps.Provider.ACTIVE_ACCOUNT_ID,
                                                             Imps.Provider.ACTIVE_ACCOUNT_USERNAME,
                                                             Imps.Provider.ACTIVE_ACCOUNT_PW,
                                                             Imps.Provider.ACTIVE_ACCOUNT_LOCKED,
                                                             Imps.Provider.ACTIVE_ACCOUNT_KEEP_SIGNED_IN,
                                                             Imps.Provider.ACCOUNT_PRESENCE_STATUS,
                                                             Imps.Provider.ACCOUNT_CONNECTION_STATUS
                                                            };

        static final int PROVIDER_ID_COLUMN = 0;
        static final int PROVIDER_NAME_COLUMN = 1;
        static final int PROVIDER_FULLNAME_COLUMN = 2;
        static final int PROVIDER_CATEGORY_COLUMN = 3;
        static final int ACTIVE_ACCOUNT_ID_COLUMN = 4;
        static final int ACTIVE_ACCOUNT_USERNAME_COLUMN = 5;
        static final int ACTIVE_ACCOUNT_PW_COLUMN = 6;
        static final int ACTIVE_ACCOUNT_LOCKED = 7;
        static final int ACTIVE_ACCOUNT_KEEP_SIGNED_IN = 8;
        static final int ACCOUNT_PRESENCE_STATUS = 9;
        static final int ACCOUNT_CONNECTION_STATUS = 10;

        private static final int ACCOUNT_LOADER_ID = 1000;

    }