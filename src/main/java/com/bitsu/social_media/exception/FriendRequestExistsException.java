package com.bitsu.social_media.exception;

public class FriendRequestExistsException extends RuntimeException{
    public FriendRequestExistsException(String message) {
        super(message);
    }
}
