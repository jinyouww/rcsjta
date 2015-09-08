/*******************************************************************************
 * Software Name : RCS IMS Stack
 *
 * Copyright (C) 2010 France Telecom S.A.
 * Copyright (C) 2014 Sony Mobile Communications Inc.
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
 *
 * NOTE: This file has been modified by Sony Mobile Communications Inc.
 * Modifications are licensed under the License.
 ******************************************************************************/

package com.gsma.rcs.provider.ipcall;

import com.gsma.rcs.core.content.AudioContent;
import com.gsma.rcs.core.content.VideoContent;
import com.gsma.rcs.provider.CursorUtil;
import com.gsma.rcs.provider.LocalContentResolver;
import com.gsma.rcs.service.ipcalldraft.IPCall.ReasonCode;
import com.gsma.rcs.service.ipcalldraft.IPCall.State;
import com.gsma.rcs.utils.logger.Logger;
import com.gsma.services.rcs.RcsService.Direction;
import com.gsma.services.rcs.contact.ContactId;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * IP call history
 * 
 * @author owom5460
 */
public class IPCallHistory {
    /**
     * Current instance
     */
    private static IPCallHistory sInstance;

    private final LocalContentResolver mLocalContentResolver;

    /**
     * The logger
     */
    private static final Logger logger = Logger.getLogger(IPCallHistory.class.getSimpleName());

    private static final int FIRST_COLUMN_IDX = 0;

    /**
     * Get IPCall session info from its unique Id
     * 
     * @param columnName
     * @param callId
     * @return Cursor the caller of this method has to close the cursor if a cursor is returned
     */
    private Cursor getIPCallData(String columnName, String callId) {
        String[] projection = new String[] {
            columnName
        };
        Uri contentUri = Uri.withAppendedPath(IPCallData.CONTENT_URI, callId);
        Cursor cursor = mLocalContentResolver.query(contentUri, projection, null, null, null);
        CursorUtil.assertCursorIsNotNull(cursor, contentUri);
        if (!cursor.moveToNext()) {
            CursorUtil.close(cursor);
            return null;
        }
        return cursor;
    }

    private Integer getDataAsInteger(Cursor cursor) {
        try {
            if (cursor.isNull(FIRST_COLUMN_IDX)) {
                return null;
            }
            return cursor.getInt(FIRST_COLUMN_IDX);

        } finally {
            CursorUtil.close(cursor);
        }
    }

    /**
     * Create instance
     * 
     * @param localContentResolver Local content resolver
     * @return IPCallHistory instance
     */
    public static IPCallHistory createInstance(LocalContentResolver localContentResolver) {
        synchronized (IPCallHistory.class) {
            if (sInstance == null) {
                sInstance = new IPCallHistory(localContentResolver);
            }
            return sInstance;
        }
    }

    /**
     * Constructor
     * 
     * @param localContentResolver Local content resolver
     */
    private IPCallHistory(LocalContentResolver localContentResolver) {
        super();
        mLocalContentResolver = localContentResolver;
    }

    /**
     * Add a new entry in the call history
     * 
     * @param callId Call ID
     * @param contact Remote contact Id
     * @param direction Direction
     * @param audiocontent Audio content
     * @param videocontent Video content
     * @param state Call state
     * @param reasonCode
     * @param timestamp Local timestamp for both incoming and outgoing call
     * @return Uri
     */
    public Uri addCall(String callId, ContactId contact, Direction direction,
            AudioContent audiocontent, VideoContent videocontent, State state,
            ReasonCode reasonCode, long timestamp) {
        if (logger.isActivated()) {
            logger.debug(new StringBuilder("Add new call entry for contact ").append(contact)
                    .append(": call=").append(callId).append(", state=").append(state)
                    .append(", reasonCode =").append(reasonCode).toString());
        }

        ContentValues values = new ContentValues();
        values.put(IPCallData.KEY_CALL_ID, callId);
        values.put(IPCallData.KEY_CONTACT, contact.toString());
        values.put(IPCallData.KEY_DIRECTION, direction.toInt());
        values.put(IPCallData.KEY_TIMESTAMP, timestamp);
        values.put(IPCallData.KEY_DURATION, 0);
        values.put(IPCallData.KEY_STATE, state.toInt());
        values.put(IPCallData.KEY_REASON_CODE, reasonCode.toInt());
        if (videocontent != null) {
            values.put(IPCallData.KEY_VIDEO_ENCODING, videocontent.getEncoding());
            values.put(IPCallData.KEY_WIDTH, videocontent.getWidth());
            values.put(IPCallData.KEY_HEIGHT, videocontent.getHeight());
        } else {
            values.put(IPCallData.KEY_WIDTH, 0);
            values.put(IPCallData.KEY_HEIGHT, 0);
        }
        if (audiocontent != null) {
            values.put(IPCallData.KEY_AUDIO_ENCODING, audiocontent.getEncoding());
        }
        return mLocalContentResolver.insert(IPCallData.CONTENT_URI, values);
    }

    /**
     * Set the call state and reason code
     * 
     * @param callId Call ID
     * @param state New state
     * @param reasonCode Reason code
     * @return True if updated
     */
    public boolean setCallStateAndReasonCode(String callId, State state, ReasonCode reasonCode) {
        if (logger.isActivated()) {
            logger.debug(new StringBuilder("Update call state of call ").append(callId)
                    .append(" state=").append(state).append(", reasonCode=").append(reasonCode)
                    .toString());
        }

        ContentValues values = new ContentValues();
        values.put(IPCallData.KEY_STATE, state.toInt());
        values.put(IPCallData.KEY_REASON_CODE, reasonCode.toInt());
        return mLocalContentResolver.update(Uri.withAppendedPath(IPCallData.CONTENT_URI, callId),
                values, null, null) > 0;
    }

    /**
     * Delete all entries in IP call history
     */
    public void deleteAllEntries() {
        mLocalContentResolver.delete(IPCallData.CONTENT_URI, null, null);
    }

    /**
     * Get IPCall session state from unique Id
     * 
     * @param callId
     * @return State
     */
    public State getState(String callId) {
        if (logger.isActivated()) {
            logger.debug("Get IP call state for callId ".concat(callId));
        }
        Cursor cursor = getIPCallData(IPCallData.KEY_STATE, callId);
        if (cursor == null) {
            return null;
        }
        return State.valueOf(getDataAsInteger(cursor));
    }

    /**
     * Get IPCall session reason code from unique Id
     * 
     * @param callId
     * @return Reason code
     */
    public ReasonCode getReasonCode(String callId) {
        if (logger.isActivated()) {
            logger.debug("Get IP call reason code for callId ".concat(callId));
        }
        Cursor cursor = getIPCallData(IPCallData.KEY_REASON_CODE, callId);
        if (cursor == null) {
            return null;
        }
        return ReasonCode.valueOf(getDataAsInteger(cursor));
    }

    /**
     * Get IPCall session info from its unique Id
     * 
     * @param callId
     * @return Cursor the caller of this method has to close the cursor if a cursor is returned
     */
    public Cursor getIPCallData(String callId) {
        Uri contentUri = Uri.withAppendedPath(IPCallData.CONTENT_URI, callId);
        Cursor cursor = mLocalContentResolver.query(contentUri, null, null, null, null);
        CursorUtil.assertCursorIsNotNull(cursor, contentUri);
        return cursor;
    }
}
