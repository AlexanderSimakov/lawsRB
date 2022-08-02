package com.team.lawsrb.ui.informationViewers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.team.lawsrb.R

object CardViewFactory {

    fun getLightCard(context: Context,
                     title: String,
                     content: String): MaterialCardView {
        val card = LayoutInflater.from(context)
            .inflate(R.layout.custom_card_views, null).findViewById<MaterialCardView>(R.id.light_card)

        if (card.parent != null){
            (card.parent as ViewGroup).removeView(card)
        }

        card.findViewById<TextView>(R.id.light_card_title).text = title
        card.findViewById<TextView>(R.id.light_card_content).text = content

        return card
    }

    fun getDarkCard(context: Context,
                     title: String,
                     content: String): MaterialCardView {
        val card = LayoutInflater.from(context)
            .inflate(R.layout.custom_card_views, null).findViewById<MaterialCardView>(R.id.dark_card)

        if (card.parent != null){
            (card.parent as ViewGroup).removeView(card)
        }

        card.findViewById<TextView>(R.id.dark_card_title).text = title
        card.findViewById<TextView>(R.id.dark_card_content).text = content

        return card
    }

    fun getLightCardWithButton(context: Context,
                              title: String,
                              content: String): MaterialCardView {
        val card = LayoutInflater.from(context)
            .inflate(R.layout.custom_card_views, null).findViewById<MaterialCardView>(R.id.light_card_with_button)

        if (card.parent != null){
            (card.parent as ViewGroup).removeView(card)
        }

        card.findViewById<TextView>(R.id.light_card_with_button_title).text = title
        card.findViewById<TextView>(R.id.light_card_with_button_content).text = content

        return card
    }

    fun getDarkCardWithButton(context: Context,
                    title: String,
                    content: String): MaterialCardView {
        val card = LayoutInflater.from(context)
            .inflate(R.layout.custom_card_views, null).findViewById<MaterialCardView>(R.id.dark_card_with_button)

        if (card.parent != null){
            (card.parent as ViewGroup).removeView(card)
        }

        card.findViewById<TextView>(R.id.dark_card_with_button_title).text = title
        card.findViewById<TextView>(R.id.dark_card_with_button_content).text = content

        return card
    }

}