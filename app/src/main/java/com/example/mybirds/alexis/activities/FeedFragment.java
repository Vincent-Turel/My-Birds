package com.example.mybirds.alexis.activities;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mybirds.R;
import com.example.mybirds.alexis.adapters.PostAdapter;
import com.example.mybirds.alexis.adapters.StoryAdapter;
import com.example.mybirds.alexis.factories.AlbertPostFactory;
import com.example.mybirds.alexis.factories.BrunoPostFactory;
import com.example.mybirds.alexis.factories.PostFactory;
import com.example.mybirds.alexis.factories.RobertPostFactory;
import com.example.mybirds.alexis.factories.postcontent.ContentPost;
import com.example.mybirds.alexis.factories.postcreator.UserPost;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;


public class FeedFragment extends Fragment {

    View view;
    RecyclerView storyRecyclerView;
    RecyclerView postRecyclerView;

    StoryAdapter storyAdapter;
    PostAdapter postAdapter;

    int[] img_story;

    List<UserPost> postCreators;
    List<ContentPost> postContents;
    int[] img_post;
    int[] img_display;
    String[] txt_name;
    String[] txt_time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createNotificationChannel("Channel test","channel test",NotificationManager.IMPORTANCE_HIGH);

        this.view = getView();
        postCreators = new ArrayList<>();
        postContents = new ArrayList<>();

        final Button button = view.findViewById(R.id.button_post);
        button.setOnClickListener(e -> addPost());

        intializeStory();
        initializePosts();

        setStory();
        setPosts();
    }

    private void initializePosts(){
        postRecyclerView = view.findViewById(R.id.rv_post);
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void intializeStory(){
        storyRecyclerView = view.findViewById(R.id.rv_story);
        storyRecyclerView.setHasFixedSize(true);
        storyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
    }

    private void addPost(){
        int random = ThreadLocalRandom.current().nextInt(0, 3);
        PostFactory post;
        switch (random) {
            case 1 :
                post = new BrunoPostFactory();
                break;
            case 2 :
                post = new RobertPostFactory();
                break;
            case 0 :
            default :
                post = new AlbertPostFactory();
                break;
        }
        postCreators.add(post.getPostCreator());
        postContents.add(post.getPostContent());
        sendNotificationOnChannel("Incoyable !",
                postCreators.get(postCreators.size() - 1).getTxt_username() + " à posté une nouvelle photo !",
                CHANNEL_ID, NotificationCompat.PRIORITY_HIGH, postContents.get(postContents.size() - 1).getImg_post());
        setPosts();
    }

    private void setStory(){
        img_story =  new int[] {R.drawable.albert, R.drawable.bruno, R.drawable.robert, R.drawable.randonneur, R.drawable.albert, R.drawable.bruno, R.drawable.robert};
        storyAdapter = new StoryAdapter(img_story,getActivity());
        storyRecyclerView.setAdapter(storyAdapter);

    }

    private void setPosts() {
        postAdapter = new PostAdapter(postCreators, postContents, getActivity());
        postRecyclerView.setAdapter(postAdapter);
    }

    private static NotificationManager notificationManager;
    public static final String CHANNEL_ID = "channel";

    private void sendNotificationOnChannel(String title, String message, String channelId, int priority, int picture){
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getContext(),channelId)
            .setSmallIcon(R.drawable.ic_message)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(priority);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), picture);
        notification.setLargeIcon(bitmap).setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));
        NotificationManagerCompat.from(getContext()).notify(0,notification.build());
    }

    private void createNotificationChannel(String name, String description, int importance){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }
    }
}