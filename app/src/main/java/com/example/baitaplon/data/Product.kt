package com.example.baitaplon.data
import android.os.Parcel
import android.os.Parcelable

class Product(
    var id: Int? = null,
    var imageLink: String? = null,
    var productName: String? = null,
    var price: Int? = null,
    var brand: String? = null,
    var yearOfManufacture: Int? = null,
    var description: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(imageLink)
        parcel.writeString(productName)
        parcel.writeValue(price)
        parcel.writeString(brand)
        parcel.writeValue(yearOfManufacture)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}