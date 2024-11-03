package com.example.soundme.constant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.soundme.MyApplication;
import com.example.soundme.models.Song;
import com.example.soundme.models.UserInfor;
import com.example.soundme.prefs.DataStoreManager;
import com.example.soundme.service.MusicReceiver;
import com.example.soundme.service.MusicService;

import java.util.ArrayList;
import java.util.List;


public class GlobalFuntion {

    public static void startActivity(Context context, Class<?> clz) {
        Intent intent = new Intent(context, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

//    public static String getTextSearch(String input) {
//        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
//        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
//        return pattern.matcher(nfdNormalizedString).replaceAll("");
//    }
//
    public static void startMusicService(Context ctx, int action, int songPosition) {
        Intent musicService = new Intent(ctx, MusicService.class);
        musicService.putExtra(Constant.MUSIC_ACTION, action);
        musicService.putExtra(Constant.SONG_POSITION, songPosition);
        ctx.startService(musicService);
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    public static PendingIntent openMusicReceiver(Context ctx, int action) {
        Intent intent = new Intent(ctx, MusicReceiver.class);
        intent.putExtra(Constant.MUSIC_ACTION, action);
        int pendingFlag = PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT;
        return PendingIntent.getBroadcast(ctx.getApplicationContext(), action, intent, pendingFlag);
    }

    public static boolean isFavoriteSong(Song song) {
        if (song.getFavorite() == null || song.getFavorite().isEmpty()) return false;
        List<UserInfor> listUsersFavorite = new ArrayList<>(song.getFavorite().values());
        if (listUsersFavorite.isEmpty()) return false;
        for (UserInfor userInfor : listUsersFavorite) {
            if (DataStoreManager.getUser().getEmail().equals(userInfor.getEmailUser())) {
                return true;
            }
        }
        return false;
    }

    public static UserInfor getUserFavoriteSong(Song song) {
        UserInfor userInfor = null;
        if (song.getFavorite() == null || song.getFavorite().isEmpty()) return null;
        List<UserInfor> listUsersFavorite = new ArrayList<>(song.getFavorite().values());
        if (listUsersFavorite.isEmpty()) return null;
        for (UserInfor userObject : listUsersFavorite) {
            if (DataStoreManager.getUser().getEmail().equals(userObject.getEmailUser())) {
                userInfor = userObject;
                break;
            }
        }
        return userInfor;
    }

    public static void onClickFavoriteSong(Context context, Song song, boolean isFavorite) {
        if (context == null) return;
        if (isFavorite) {
            String userEmail = DataStoreManager.getUser().getEmail();
            UserInfor userInfor = new UserInfor(System.currentTimeMillis(), userEmail);
            MyApplication.get(context).getSongsDatabaseReference()
                    .child(String.valueOf(song.getId()))
                    .child("favorite")
                    .child(String.valueOf(userInfor.getId()))
                    .setValue(userInfor);
        } else {
            UserInfor userInfor = getUserFavoriteSong(song);
            if (userInfor != null) {
                MyApplication.get(context).getSongsDatabaseReference()
                        .child(String.valueOf(song.getId()))
                        .child("favorite")
                        .child(String.valueOf(userInfor.getId()))
                        .removeValue();
            }
        }
    }
//
//    @SuppressLint("InflateParams")
//    public static void handleClickMoreOptions(Activity context, Song song) {
//        if (context == null || song == null) return;
//
//        LayoutBottomSheetOptionBinding binding = LayoutBottomSheetOptionBinding
//                .inflate(LayoutInflater.from(context));
//
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
//        bottomSheetDialog.setContentView(binding.getRoot());
//        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
//
//        GlideUtils.loadUrl(song.getImage(), binding.imgSong);
//        binding.tvSongName.setText(song.getTitle());
//        binding.tvArtist.setText(song.getArtist());
//
//        if (MusicService.isSongExist(song.getId())) {
//            binding.layoutRemovePlaylist.setVisibility(View.VISIBLE);
//            binding.layoutPriority.setVisibility(View.VISIBLE);
//            binding.layoutAddPlaylist.setVisibility(View.GONE);
//        } else {
//            binding.layoutRemovePlaylist.setVisibility(View.GONE);
//            binding.layoutPriority.setVisibility(View.GONE);
//            binding.layoutAddPlaylist.setVisibility(View.VISIBLE);
//        }
//
//        binding.layoutDownload.setOnClickListener(view -> {
//            MainActivity mainActivity = (MainActivity) context;
//            mainActivity.downloadSong(song);
//            bottomSheetDialog.hide();
//        });
//
//        binding.layoutPriority.setOnClickListener(view -> {
//            if (MusicService.isSongPlaying(song.getId())) {
//                showToastMessage(context, context.getString(R.string.msg_song_playing));
//            } else {
//                for (Song songEntity : MusicService.mListSongPlaying) {
//                    songEntity.setPriority(songEntity.getId() == song.getId());
//                }
//                showToastMessage(context, context.getString(R.string.msg_setting_priority_successfully));
//            }
//            bottomSheetDialog.hide();
//        });
//
//        binding.layoutAddPlaylist.setOnClickListener(view -> {
//            if (MusicService.mListSongPlaying == null || MusicService.mListSongPlaying.isEmpty()) {
//                MusicService.clearListSongPlaying();
//                MusicService.mListSongPlaying.add(song);
//                MusicService.isPlaying = false;
//                GlobalFuntion.startMusicService(context, Constant.PLAY, 0);
//                GlobalFuntion.startActivity(context, PlayMusicActivity.class);
//            } else {
//                MusicService.mListSongPlaying.add(song);
//                showToastMessage(context, context.getString(R.string.msg_add_song_playlist_success));
//            }
//            bottomSheetDialog.hide();
//        });
//
//        binding.layoutRemovePlaylist.setOnClickListener(view -> {
//            if (MusicService.isSongPlaying(song.getId())) {
//                showToastMessage(context, context.getString(R.string.msg_cannot_delete_song));
//            } else {
//                MusicService.deleteSongFromPlaylist(song.getId());
//                showToastMessage(context, context.getString(R.string.msg_delete_song_from_playlist_success));
//            }
//            bottomSheetDialog.hide();
//        });
//
//        bottomSheetDialog.show();
//    }
//
//    public static void startDownloadFile(Activity activity, Song song) {
//        if (activity == null || song == null || StringUtil.isEmpty(song.getUrl())) return;
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(song.getUrl()));
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
//                | DownloadManager.Request.NETWORK_WIFI);
//        request.setTitle(activity.getString(R.string.title_download));
//        request.setDescription(activity.getString(R.string.message_download));
//
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        String fileName = song.getTitle() + ".mp3";
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//
//        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
//        if (downloadManager != null) {
//            downloadManager.enqueue(request);
//        }
//    }
}