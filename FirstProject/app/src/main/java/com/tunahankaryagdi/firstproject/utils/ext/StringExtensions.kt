package com.tunahankaryagdi.firstproject.utils.ext


fun String.getImageUrlFromPath(): String {
    return "https://image.tmdb.org/t/p/original/$this"
}