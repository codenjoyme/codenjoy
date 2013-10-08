package com.apofig;

import java.util.Map;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 5:19
 */
public interface Command {

    void apply(Map<Face, FaceValue> cube);
}
