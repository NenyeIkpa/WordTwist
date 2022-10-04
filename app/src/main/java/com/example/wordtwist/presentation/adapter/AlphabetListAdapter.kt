package com.example.wordtwist.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wordtwist.R
import com.example.wordtwist.utils.OnAlphabetClickListener


class AlphabetListAdapter( private val onAlphabetClickListener: OnAlphabetClickListener): RecyclerView.Adapter<AlphabetListAdapter.AlphabetViewHolder>() {

    //Generate a character range from A-Z and convert to a list
    private val alphabetList = ('A').rangeTo('Z').toList()



    class AlphabetViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val alphabetTap: Button = view.findViewById(R.id.alphabet_btn)
    }

//    creates new views using given layout as template
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlphabetViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.alphabet_list_item, parent, false)
//        val layout = AlphabetListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // Setup custom accessibility delegate to set the text read
//        layout.accessibilityDelegate = Accessibility
        return AlphabetViewHolder(layout)
    }

    override fun getItemCount(): Int = alphabetList.size

    //    Replaces the content of an existing view with new data
    override fun onBindViewHolder(holder: AlphabetViewHolder, position: Int) {
        val item = alphabetList[position]
        holder.alphabetTap.text = item.toString()


//        pass the alphabet from the view clicked as an argument to SelectByAlphabetTwistFragment
        holder.itemView.setOnClickListener {
            onAlphabetClickListener.onAlphabetClicked(holder.alphabetTap.text.toString())

//            val action = AlphabetListFragmentDirections.actionAlphabetListFragmentToSelectByAlphabetTwistFragment(alphabet = holder.alphabetTap.text.toString())
//            holder.itemView.findNavController().navigate(action)
        }
    }

    // Setup custom accessibility delegate to set the text read with
    // an accessibility service
//    companion object Accessibility : View.AccessibilityDelegate() {
//        override fun onInitializeAccessibilityNodeInfo(
//            host: View?,
//            info: AccessibilityNodeInfo?
//        ) {
//            super.onInitializeAccessibilityNodeInfo(host, info)
//            // With `null` as the second argument to [AccessibilityAction], the
//            // accessibility service announces "double tap to activate".
//            // If a custom string is provided,
//            // it announces "double tap to <custom string>".
//            val customString = host?.context?.getString(R.string.look_up_words)
//            val customClick =
//                AccessibilityNodeInfo.AccessibilityAction(
//                    AccessibilityNodeInfo.ACTION_CLICK,
//                    customString
//                )
//            info?.addAction(customClick)
//        }
//    }
}