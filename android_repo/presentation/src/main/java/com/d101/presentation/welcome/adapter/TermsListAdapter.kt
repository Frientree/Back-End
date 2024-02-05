package com.d101.presentation.welcome.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.d101.presentation.databinding.ItemTermsAgreeBinding
import com.d101.presentation.model.TermsItem

class TermsListAdapter(
    private val checkTerms: (TermsItem) -> Unit,
) : ListAdapter<TermsItem, TermsListAdapter.TermsViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TermsViewHolder {
        val binding = ItemTermsAgreeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return TermsViewHolder(binding, checkTerms)
    }

    override fun onBindViewHolder(holder: TermsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TermsViewHolder(
        private val binding: ItemTermsAgreeBinding,
        checkTerms: (TermsItem) -> Unit,
    ) : ViewHolder(binding.root) {

        private lateinit var termsItem: TermsItem

        init {
            binding.termsCheckBox.setOnClickListener {
                checkTerms(termsItem.copy(checked = termsItem.checked.not()))
            }
        }

        fun bind(termsItem: TermsItem) {
            this.termsItem = termsItem
            binding.termsItem = termsItem
        }
    }

    companion object {
        val diffUtil = object : ItemCallback<TermsItem>() {
            override fun areItemsTheSame(oldItem: TermsItem, newItem: TermsItem): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: TermsItem, newItem: TermsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
