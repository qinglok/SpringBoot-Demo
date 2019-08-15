package xyz.beingx.basespring.ext

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Any.logger(): Logger = LoggerFactory.getLogger(this::class.java)