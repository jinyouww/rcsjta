/*******************************************************************************
 * Software Name : RCS IMS Stack
 *
 * Copyright (C) 2010-2016 Orange.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.gsma.rcs.ri.sharing;

import com.gsma.rcs.ri.R;
import com.gsma.rcs.ri.sharing.geoloc.InitiateGeolocSharing;
import com.gsma.rcs.ri.sharing.image.InitiateImageSharing;
import com.gsma.rcs.ri.sharing.video.OutgoingVideoSharing;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Sharing API
 * 
 * @author Jean-Marc AUFFRET
 */
public class TestSharingApi extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Set items
        // @formatter:off
        String[] items = {
                getString(R.string.menu_initiate_image_sharing),
                getString(R.string.menu_initiate_video_sharing),
                getString(R.string.menu_initiate_geoloc_sharing),
                getString(R.string.menu_log_sharing)
        };
        // @formatter:on

        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, InitiateImageSharing.class));
                break;

            case 1:
                startActivity(new Intent(this, OutgoingVideoSharing.class));
                break;

            case 2:
                startActivity(new Intent(this, InitiateGeolocSharing.class));
                break;

            case 3:
                startActivity(new Intent(this, SharingListView.class));
                break;
        }
    }
}
