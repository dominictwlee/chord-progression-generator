package com.domtwlee.chordprogressiongenerator

data class ChordGenParams (
    var length: Int = 10,
    var start: Int = 1,
    var end: Int = 7,
    var type: String = "minor")