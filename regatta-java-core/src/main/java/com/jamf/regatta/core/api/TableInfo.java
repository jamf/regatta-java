/*
 * Copyright JAMF Software, LLC
 */

package com.jamf.regatta.core.api;

import java.util.Map;

public record TableInfo(String id, String name, Map<String, Object> config) {
}
