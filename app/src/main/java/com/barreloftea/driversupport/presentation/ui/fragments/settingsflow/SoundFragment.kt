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
        for(i in 0..fields.size-1){
            adapter.add(fields[i].name)
        }
        binding.listViewSignalSound.adapter = adapter
        binding.listViewSignalSound.setOnItemClickListener { parent, view, position, id ->
            //TODO getIdentifier is resource-consuming
            viewModel.saveSignalSoundResId(requireActivity().resources.getIdentifier(adapter.getItem(position)!!, "raw", "com.barreloftea.driversupport.domain"))
            navController.navigateSafely(R.id.action_sound_to_settings)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController();

    }

}