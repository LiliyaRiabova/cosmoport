package com.space.controller;

public class ShipNotFoundException extends RuntimeException {

    ShipNotFoundException(Long id) {
        super("Could not find employee " + id);
    }
}
