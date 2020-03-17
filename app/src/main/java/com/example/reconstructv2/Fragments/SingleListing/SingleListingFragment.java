package com.example.reconstructv2.Fragments.SingleListing;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reconstructv2.MainNavGraphDirections;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.R;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;


public class SingleListingFragment extends Fragment {

    private String listingID;
    private ListingFull listingData;
    private RecyclerView itemRecyclerView;
    private ListingItemAdapter recyclerAdapter;



    private SingleListingFragment.OnFragmentInteractionListener mListener;

    private SwipeRefreshLayout refreshLayout;
    private TextView titleTextView;
    private TextView bodyTextView;
    private ImageView listingImage;

    private Button viewAuthorAccountButton;
    private Button viewLocationButton;
    private Button reserveButton;

    private SingleListingViewModel viewModel;

    public SingleListingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_single_listing,container,false);
    }

    @Override
    public  void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);
        final View view = getView();

        initViews(view);
        initViewModel();
        setOnClickListeners();
        setLiveDataObservers();
        configureRecyclerViewAdapter();
        setRefreshListener();

        // if the user navigated without a full listing, fetch the whole listing
        if (SingleListingFragmentArgs.fromBundle(getArguments()).getShouldRefresh()) {
            listingID = SingleListingFragmentArgs.fromBundle(getArguments()).getListingID();
            getListingRequest(listingID);


        } else {
            // extract and display the full listing
            listingData = SingleListingFragmentArgs.fromBundle(getArguments()).getListingFullArg();
            listingID = listingData.getListingID();
            setListing(listingData);
            recyclerAdapter.setListingItems(listingData.getItemList());
        }
    }

    private void initViews(View view){
        titleTextView = view.findViewById(R.id.singleListingTitleTextView);
        bodyTextView = view.findViewById(R.id.singleListingBodyTextView);
        itemRecyclerView = view.findViewById(R.id.singleListingRecyclerView);

        refreshLayout = view.findViewById(R.id.singleListingSwipeRefreshLayout);
        listingImage = view.findViewById(R.id.singleListingImageView);

        reserveButton = view.findViewById(R.id.singleListingReserveButton);
        viewAuthorAccountButton = view.findViewById(R.id.singleListing_ViewUserAccountButton);
        viewLocationButton = view.findViewById(R.id.singleListing_ViewListingLocationButton);
    }

    private void setRefreshListener(){

        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            getListingRequest(listingID);
        });
    }

    private void setOnClickListeners(){

        reserveButton.setOnClickListener(v -> {

            // check if the full listing has been fethched
            if (listingData instanceof ListingFull) {
                refreshLayout.setRefreshing(true);
                viewModel.reserveItemsRequest(getContext(),listingData);

            } else {
                Toast.makeText(getContext() , "please wait for the listing to be fetched from the server", Toast.LENGTH_SHORT).show();
            }
        });

        viewAuthorAccountButton.setOnClickListener(v -> {
            // navigate to view author account fragment
            MainNavGraphDirections.ActionGlobalAccountViewFragment3 action = MainNavGraphDirections.actionGlobalAccountViewFragment3(listingData.getAuthorID());
            Navigation.findNavController(getView()).navigate(action);

        });

        viewLocationButton.setOnClickListener(v -> {
            SingleListingFragmentDirections.ActionSingleListingFragmentToLocationViewFragment action = SingleListingFragmentDirections.actionSingleListingFragmentToLocationViewFragment( new LatLng(listingData.getLocation_lat(),listingData.getLocation_lon()));
            Navigation.findNavController(getView()).navigate(action);
        });


    }

    private void configureRecyclerViewAdapter(){

      recyclerAdapter = new ListingItemAdapter(getContext());
      itemRecyclerView.setAdapter(recyclerAdapter);

      itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      itemRecyclerView.setHasFixedSize(true);


      // navigate to single item fragment
      recyclerAdapter.setOnItemCLickListener(listingItem -> {
          Integer itemPosition = listingData.getItemList().indexOf(listingItem);
          SingleListingFragmentDirections.ActionSingleListingFragmentToSingleItemViewFragment action = SingleListingFragmentDirections.actionSingleListingFragmentToSingleItemViewFragment(listingData,itemPosition);
          Navigation.findNavController(getView()).navigate(action);
      });

      // select an item
      recyclerAdapter.setLongClickListener(listingItem -> {
          // check if available
          if (listingItem.getAvailable()){
              listingItem.toggleIsSelected();
              recyclerAdapter.notifyDataSetChanged();
          } else {
              Toast.makeText(getContext(), "this item is un available", Toast.LENGTH_SHORT).show();
          }

      });
    };

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(SingleListingViewModel.class);
    }

    private void getListingRequest(String listingID){
        viewModel.fetchListing(getContext(),listingID);
        viewModel.getListingLiveData().observe(getViewLifecycleOwner(), response -> {
            refreshLayout.setRefreshing(false);

            if (response.getIsSuccesfull()){
                listingData = response.getListing();
                setListing(listingData);
                recyclerAdapter.setListingItems(listingData.getItemList());
                recyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setLiveDataObservers(){
        viewModel.getBaseAPIResponseLiveData().observe(getViewLifecycleOwner(), response -> {
            refreshLayout.setRefreshing(false);

            if (response.getIsSuccesfull()){
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // display a listing
    private void setListing(ListingFull listing){

        titleTextView.setText(listing.getTitle());
        bodyTextView.setText(listing.getBody());

        try {
            String rootURL = getContext().getResources().getString(R.string.ROOTURL);
            String imageUrl = rootURL + "/getImage?imageID=" + listing.getMainImageID();
            Picasso.get().load(imageUrl).into(listingImage);
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SingleListingFragment.OnFragmentInteractionListener) {
            mListener = (SingleListingFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
