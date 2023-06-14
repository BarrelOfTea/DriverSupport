package com.barreloftea.driversupport.presentation.ui.fragments.settingsflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.barreloftea.driversupport.R
import com.barreloftea.driversupport.databinding.FragmentSettingsChooseSoundBinding
import com.barreloftea.driversupport.presentation.navutils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SoundFragment: Fragment() {

    private lateinit var binding : FragmentSettingsChooseSoundBinding
    private lateinit var navController: NavController
    private val viewModel : SettingsViewModel by viewModels()
    private lateinit var adapter : ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSettingsChooseSoundBinding.inflate(inflater, container, false);


        adapter = ArrayAdapter(requireActivity(), R.layout.item_sound, ArrayList<String>())
        val fields = com.barreloftea.driversupport.domain.R.raw::class.java.fields
        for(element in fields){
            adapter.add(element.name)
        }
        binding.listViewSignalSound.adapter = adapter
        binding.listViewSignalSound.setOnItemClickListener { parent, view, position, id ->
            viewModel.saveSignalSoundResId(fields[position].getInt(fields[position]))
            navController.navigateSafely(R.id.action_sound_to_settings)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController();

    }

}



//getIdentifier is resource-consuming
//viewModel.saveSignalSoundResId(requireActivity().resources.getIdentifier(adapter.getItem(position)!!, "raw", "com.barreloftea.driversupport.domain"))