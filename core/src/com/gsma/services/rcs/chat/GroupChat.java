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

package com.gsma.services.rcs.chat;

import com.gsma.services.rcs.Geoloc;
import com.gsma.services.rcs.RcsService.Direction;
import com.gsma.services.rcs.RcsServiceException;
import com.gsma.services.rcs.contacts.ContactId;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Group chat
 * 
 * @author Jean-Marc AUFFRET
 */
public class GroupChat {
    /**
     * Group chat state
     */
    public enum State {

        /**
         * Chat invitation received
         */
        INVITED(0),

        /**
         * Chat invitation sent
         */
        INITIATING(1),

        /**
         * Chat is started
         */
        STARTED(2),

        /**
         * Chat has been aborted
         */
        ABORTED(3),

        /**
         * Chat has failed
         */
        FAILED(4),

        /**
         * Chat has been accepted and is in the process of becoming started.
         */
        ACCEPTING(5),

        /**
         * Chat invitation was rejected.
         */
        REJECTED(6);

        private final int mValue;

        private static SparseArray<State> mValueToEnum = new SparseArray<State>();
        static {
            for (State entry : State.values()) {
                mValueToEnum.put(entry.toInt(), entry);
            }
        }

        private State(int value) {
            mValue = value;
        }

        public final int toInt() {
            return mValue;
        }

        public final static State valueOf(int value) {
            State entry = mValueToEnum.get(value);
            if (entry != null) {
                return entry;
            }
            throw new IllegalArgumentException(new StringBuilder("No enum const class ")
                    .append(State.class.getName()).append(".").append(value).append("!").toString());
        }
    }

    /**
     * Group chat state reason code
     */
    public enum ReasonCode {

        /**
         * No specific reason code specified.
         */
        UNSPECIFIED(0),

        /**
         * Group chat is aborted by local user.
         */
        ABORTED_BY_USER(1),

        /**
         * Group chat is aborted by remote user.
         */
        ABORTED_BY_REMOTE(2),

        /**
         * Group chat is aborted by inactivity.
         */
        ABORTED_BY_INACTIVITY(3),

        /**
         * Group chat is rejected because already taken by the secondary device.
         */
        REJECTED_BY_SECONDARY_DEVICE(4),

        /**
         * Group chat invitation was rejected as it was detected as spam.
         */
        REJECTED_SPAM(5),

        /**
         * Group chat invitation was rejected due to max number of chats open already.
         */
        REJECTED_MAX_CHATS(6),

        /**
         * Group chat invitation was rejected by remote.
         */
        REJECTED_BY_REMOTE(7),

        /**
         * Group chat invitation was rejected by inactivity.
         */
        REJECTED_BY_INACTIVITY(8),

        /**
         * Group chat initiation failed.
         */
        FAILED_INITIATION(9);

        private final int mValue;

        private static SparseArray<ReasonCode> mValueToEnum = new SparseArray<ReasonCode>();
        static {
            for (ReasonCode entry : ReasonCode.values()) {
                mValueToEnum.put(entry.toInt(), entry);
            }
        }

        private ReasonCode(int value) {
            mValue = value;
        }

        public final int toInt() {
            return mValue;
        }

        public final static ReasonCode valueOf(int value) {
            ReasonCode entry = mValueToEnum.get(value);
            if (entry != null) {
                return entry;
            }
            throw new IllegalArgumentException(new StringBuilder("No enum const class ")
                    .append(ReasonCode.class.getName()).append(".").append(value).append("!")
                    .toString());
        }
    }

    /**
     * Group chat interface
     */
    private final IGroupChat mGroupChatInf;

    /**
     * Constructor
     * 
     * @param chatIntf Group chat interface
     */
    /* package private */GroupChat(IGroupChat chatIntf) {
        mGroupChatInf = chatIntf;
    }

    /**
     * Returns the chat ID
     * 
     * @return Chat ID
     * @throws RcsServiceException
     */
    public String getChatId() throws RcsServiceException {
        try {
            return mGroupChatInf.getChatId();
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Returns the direction of the group chat
     * 
     * @return Direction
     * @see Direction
     * @throws RcsServiceException
     */
    public Direction getDirection() throws RcsServiceException {
        try {
            return Direction.valueOf(mGroupChatInf.getDirection());
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Returns the state of the group chat
     * 
     * @return State
     * @see State
     * @throws RcsServiceException
     */
    public int getState() throws RcsServiceException {
        try {
            return mGroupChatInf.getState();
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Returns the reason code of the state of the group chat
     * 
     * @return ReasonCode
     * @see ReasonCode
     * @throws RcsServiceException
     */
    public int getReasonCode() throws RcsServiceException {
        try {
            return mGroupChatInf.getReasonCode();
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Returns the remote contact
     * 
     * @return Contact
     * @throws RcsServiceException
     */
    public ContactId getRemoteContact() throws RcsServiceException {
        try {
            return mGroupChatInf.getRemoteContact();
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Returns the subject of the group chat
     * 
     * @return Subject
     * @throws RcsServiceException
     */
    public String getSubject() throws RcsServiceException {
        try {
            return mGroupChatInf.getSubject();
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Returns the list of connected participants. A participant is identified by its MSISDN in
     * national or international format, SIP address, SIP-URI or Tel-URI.
     * 
     * @return List of participants
     * @throws RcsServiceException
     */
    public Set<ParticipantInfo> getParticipants() throws RcsServiceException {
        try {
            return new HashSet<ParticipantInfo>(mGroupChatInf.getParticipants());
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Returns true if it is possible to send messages in the group chat right now, else returns
     * false.
     * 
     * @return boolean
     * @throws RcsServiceException
     */
    public boolean canSendMessage() throws RcsServiceException {
        try {
            return mGroupChatInf.canSendMessage();
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Sends a text message to the group
     * 
     * @param text Message
     * @return ChatMessage
     * @throws RcsServiceException
     */
    public ChatMessage sendMessage(String text) throws RcsServiceException {
        try {
            return new ChatMessage(mGroupChatInf.sendMessage(text));
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Sends a geoloc message
     * 
     * @param geoloc Geoloc info
     * @return ChatMessage
     * @throws RcsServiceException
     */
    public ChatMessage sendMessage(Geoloc geoloc) throws RcsServiceException {
        try {
            return new ChatMessage(mGroupChatInf.sendMessage2(geoloc));
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Sends an Is-composing event. The status is set to true when typing a message, else it is set
     * to false.
     * 
     * @param status Is-composing status
     * @throws RcsServiceException
     */
    public void sendIsComposingEvent(boolean status) throws RcsServiceException {
        try {
            mGroupChatInf.sendIsComposingEvent(status);
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Returns true if it is possible to invite additional participants to the group chat right now,
     * else returns false.
     * 
     * @return boolean
     * @throws RcsServiceException
     */
    public boolean canInviteParticipants() throws RcsServiceException {
        try {
            return mGroupChatInf.canInviteParticipants();
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Returns true if it is possible to invite the specified participant to the group chat right
     * now, else returns false.
     * 
     * @param ContactId participant
     * @return boolean
     * @throws RcsServiceException
     */
    public boolean canInviteParticipant(ContactId participant) throws RcsServiceException {
        try {
            return mGroupChatInf.canInviteParticipant(participant);
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Invite additional participants to this group chat.
     * 
     * @param participants List of participants
     * @throws RcsServiceException
     */
    public void inviteParticipants(Set<ContactId> participants) throws RcsServiceException {
        try {
            mGroupChatInf.inviteParticipants(new ArrayList<ContactId>(participants));
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Returns the max number of participants in the group chat. This limit is read during the
     * conference event subscription and overrides the provisioning parameter.
     * 
     * @return Number
     * @throws RcsServiceException
     */
    public int getMaxParticipants() throws RcsServiceException {
        try {
            return mGroupChatInf.getMaxParticipants();
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Returns true if it is possible to leave this group chat.
     * 
     * @return boolean
     * @throws RcsServiceException
     */
    public boolean canLeave() throws RcsServiceException {
        try {
            return mGroupChatInf.canLeave();
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * Leaves a group chat willingly and permanently. The group chat will continue between other
     * participants if there are enough participants.
     * 
     * @throws RcsServiceException
     */
    public void leave() throws RcsServiceException {
        try {
            mGroupChatInf.leave();
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }

    /**
     * open the chat conversation.<br>
     * Note: if it is an incoming pending chat session and the parameter IM SESSION START is 0 then
     * the session is accepted now.
     * 
     * @throws RcsServiceException
     */
    public void openChat() throws RcsServiceException {
        try {
            mGroupChatInf.openChat();
        } catch (Exception e) {
            throw new RcsServiceException(e.getMessage());
        }
    }
}
