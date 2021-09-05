package com.baloot.mirzazade.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import java.io.Serializable
import javax.annotation.Generated

@Module
@InstallIn(ActivityComponent::class)
@Generated("jsonschema2pojo")
class Source : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null
}