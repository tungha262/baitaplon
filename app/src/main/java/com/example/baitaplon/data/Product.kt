package com.example.baitaplon.data

class Product {
    var id : Int ?= null
    var imageLink : String ?= null
    var productName : String ?= null
    var price : Int ?= null
    var brand : String?= null
    var yearOfManufacture : Int ?= null
    var description : String ?= null

    constructor(
        id: Int?,
        imageLink: String?,
        productName: String?,
        price: Int?,
        brand: String?,
        yearOfManufacture: Int?,
        description: String?
    ) {
        this.id = id
        this.imageLink = imageLink
        this.productName = productName
        this.price = price
        this.brand = brand
        this.yearOfManufacture = yearOfManufacture
        this.description = description
    }
}