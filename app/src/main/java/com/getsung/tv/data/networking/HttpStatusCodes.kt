package com.getsung.tv.data.networking


const val OK = 200 // Standard response for successful HTTP requests
const val CREATED = 201 // Successful creation of resource

const val FOUND = 302 // Resource temporarily found



// 4xx Client Errors
const val BAD_REQUEST = 400 // Malformed request
const val UNAUTHORIZED = 401 // Authentication required
const val PAYMENT_REQUIRED = 402 // Payment required (rarely used)
const val FORBIDDEN = 403 // Server understood request but refuses to authorize
const val NOT_FOUND = 404 // Resource not found
const val CONFLICT = 409 // Conflict with current state
const val GONE = 410 // Resource permanently gone

const val LOCKED = 423 // WebDAV locked
const val TOO_MANY_REQUESTS = 429 // Too many requests

// 5xx Server Errors
const val INTERNAL_SERVER_ERROR = 500 // Generic server error
const val NOT_IMPLEMENTED = 501 // Server does not support functionality
const val BAD_GATEWAY = 502 // Invalid response from upstream server
const val SERVICE_UNAVAILABLE = 503 // Server overloaded or down
const val GATEWAY_TIMEOUT = 504 // Gateway timeout
