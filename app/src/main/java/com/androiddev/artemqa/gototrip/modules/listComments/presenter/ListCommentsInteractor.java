package com.androiddev.artemqa.gototrip.modules.listComments.presenter;

import com.androiddev.artemqa.gototrip.common.models.Comment;
import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.listComments.ContractListComments;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by artemqa on 11.04.2018.
 */

public class ListCommentsInteractor {
    private ContractListComments.Presenter mPresenter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefBaseDatabase = mDatabase.getReference();
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private StorageReference mBaseStorageReference = mStorage.getReference();

    public ListCommentsInteractor(ContractListComments.Presenter presenter) {
        mPresenter = presenter;
    }

    public void getQueryForRv(String postId) {
        Query queryKey = mRefBaseDatabase.child(Constants.POSTS_LOCATION).child(postId).child("comments").orderByValue();
        DatabaseReference refData = mRefBaseDatabase.child(Constants.COMMENTS_LOCATION);
        mPresenter.onGettingQueryForRV(queryKey, refData);
    }

    public void addCommentInPost(final String textComment, final String postId) {
        DatabaseReference refCurrentUser = mRefBaseDatabase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid());
        final DatabaseReference refCurrentPostComment = mRefBaseDatabase.child(Constants.POSTS_LOCATION).child(postId);
        final String keyComment = mRefBaseDatabase.child(Constants.COMMENTS_LOCATION).push().getKey();
        final DatabaseReference refCurrentComment = mRefBaseDatabase.child(Constants.COMMENTS_LOCATION).child(keyComment);
        refCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    final User currentUser = dataSnapshot.getValue(User.class);
                    refCurrentPostComment.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot!=null){
                                Post currentPost = dataSnapshot.getValue(Post.class);
                                currentPost.getComments().put(keyComment,true);
                                refCurrentPostComment.setValue(currentPost);
                                Comment comment = new Comment(keyComment,currentUser.getUserId(),currentUser.getName(),currentUser.getUriAvatarThumbnail(),textComment,postId);
                                refCurrentComment.setValue(comment);
                                mPresenter.onAddedComment();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
