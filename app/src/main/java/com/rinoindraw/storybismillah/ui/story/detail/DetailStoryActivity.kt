package com.rinoindraw.storybismillah.ui.story.detail

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rinoindraw.storybismillah.R
import com.rinoindraw.storybismillah.database.model.Story
import com.rinoindraw.storybismillah.databinding.ActivityDetailStoryBinding
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportPostponeEnterTransition()

        val story = intent.getParcelableExtra<Story>(EXTRA_STORY)
        initStoryData(story)

        initUI()
        initAction()
    }

    private fun initUI() {
        supportActionBar?.hide()
    }

    private fun initAction() {
        binding.apply {
            imgBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
                finish()
            }
        }
    }

    private fun initStoryData(story: Story?) {
        if (story != null) {
            binding.apply {

                Glide
                    .with(this@DetailStoryActivity)
                    .load(story.photoUrl)
                    .placeholder(R.drawable.image_tools)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            supportStartPostponedEnterTransition()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            supportStartPostponedEnterTransition()
                            return false
                        }
                    })
                    .into(imgStoryThumbnail)

                tvGreetingName.text = story.name
                tvStoryDesc.text = story.description
            }
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }

}