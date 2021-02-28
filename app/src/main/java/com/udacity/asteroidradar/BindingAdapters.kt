package com.udacity.asteroidradar

import android.graphics.drawable.VectorDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidsAdapter

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("asteroidList")
fun bindAsteroids(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidsAdapter
    adapter.submitList(data)
}

@BindingAdapter("isPotentiallyHazardous")
fun bindPotentiallyHazardous(imgView: ImageView, asteroid: Asteroid) {
    Log.d("iSandeep", "${asteroid.codename}:${asteroid.isPotentiallyHazardous}")
    imgView.setImageResource(if (asteroid.isPotentiallyHazardous)
        R.drawable.ic_status_potentially_hazardous else R.drawable.ic_status_normal)
}

@BindingAdapter("imageUrl")
fun bindImageOfTheDay(imgView: ImageView, _imgUrl: String?) {
    _imgUrl?.let {
        val imgUri = _imgUrl.toUri().buildUpon().scheme("https").build()
        Picasso.with(imgView.context)
                .load(imgUri)
                .error(R.drawable.placeholder_picture_of_day)
                .placeholder(R.drawable.placeholder_picture_of_day)
                .into(imgView)
    }
}