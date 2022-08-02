package com.team.lawsrb.ui.informationViewers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.team.lawsrb.R
import com.team.lawsrb.basic.codeObjects.Article

class ArticleContentFragment(private val article: Article) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.fragment_article_content_title)
            .text = article.title

        var content = ""
        for (item in article.items) content += item + "\n"
        view.findViewById<TextView>(R.id.fragment_article_content_text)
            .text = content

    }
}