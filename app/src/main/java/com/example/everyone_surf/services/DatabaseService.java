package com.example.everyone_surf.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.everyone_surf.model.Instructor;
import com.example.everyone_surf.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;


/// a service to interact with the Firebase Realtime Database.
/// this class is a singleton, use getInstance() to get an instance of this class
///
/// @see #getInstance()
/// @see FirebaseDatabase
public class DatabaseService {

    /// tag for logging
    ///
    /// @see Log
    private static final String TAG = "DatabaseService";

    /// paths for different data types in the database
    ///
    /// @see DatabaseService#readData(String)
    private static final String USERS_PATH = "users",
            INSTRUCTORS_PATH = "instructors",
            LESSON_PATH = "lessons";

    /// callback interface for database operations
    ///
    /// @param <T> the type of the object to return
    /// @see DatabaseCallback#onCompleted(Object)
    /// @see DatabaseCallback#onFailed(Exception)
    public interface DatabaseCallback<T> {
        /// called when the operation is completed successfully
        public void onCompleted(T object);

        /// called when the operation fails with an exception
        public void onFailed(Exception e);
    }

    /// the instance of this class
    ///
    /// @see #getInstance()
    private static DatabaseService instance;

    /// the reference to the database
    ///
    /// @see DatabaseReference
    /// @see FirebaseDatabase#getReference()
    private final DatabaseReference databaseReference;

    /// use getInstance() to get an instance of this class
    ///
    /// @see DatabaseService#getInstance()
    private DatabaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    /// get an instance of this class
    ///
    /// @return an instance of this class
    /// @see DatabaseService
    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }


    // region private generic methods
    // to write and read data from the database

    /// write data to the database at a specific path
    ///
    /// @param path     the path to write the data to
    /// @param data     the data to write (can be any object, but must be serializable, i.e. must have a default constructor and all fields must have getters and setters)
    /// @param callback the callback to call when the operation is completed
    /// @see DatabaseCallback
    private void writeData(@NotNull final String path, @NotNull final Object data, final @Nullable DatabaseCallback<Void> callback) {
        readData(path).setValue(data, (error, ref) -> {
            if (error != null) {
                if (callback == null) return;
                callback.onFailed(error.toException());
            } else {
                if (callback == null) return;
                callback.onCompleted(null);
            }
        });
    }

    /// remove data from the database at a specific path
    ///
    /// @param path     the path to remove the data from
    /// @param callback the callback to call when the operation is completed
    /// @see DatabaseCallback
    private void deleteData(@NotNull final String path, @Nullable final DatabaseCallback<Void> callback) {
        readData(path).removeValue((error, ref) -> {
            if (error != null) {
                if (callback == null) return;
                callback.onFailed(error.toException());
            } else {
                if (callback == null) return;
                callback.onCompleted(null);
            }
        });
    }

    /// read data from the database at a specific path
    ///
    /// @param path the path to read the data from
    /// @return a DatabaseReference object to read the data from
    /// @see DatabaseReference

    private DatabaseReference readData(@NotNull final String path) {
        return databaseReference.child(path);
    }


    /// get data from the database at a specific path
    ///
    /// @param path     the path to get the data from
    /// @param clazz    the class of the object to return
    /// @param callback the callback to call when the operation is completed
    /// @see DatabaseCallback
    /// @see Class
    private <T> void getData(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<T> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            T data = task.getResult().getValue(clazz);
            callback.onCompleted(data);
        });
    }

    /// get a list of data from the database at a specific path
    ///
    /// @param path     the path to get the data from
    /// @param clazz    the class of the objects to return
    /// @param callback the callback to call when the operation is completed
    private <T> void getDataList(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<List<T>> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<T> tList = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                T t = dataSnapshot.getValue(clazz);
                tList.add(t);
            });

            callback.onCompleted(tList);
        });
    }

    /// generate a new id for a new object in the database
    ///
    /// @param path the path to generate the id for
    /// @return a new id for the object
    /// @see String
    /// @see DatabaseReference#push()

    private String generateNewId(@NotNull final String path) {
        return databaseReference.child(path).push().getKey();
    }


    /// run a transaction on the data at a specific path </br>
    /// good for incrementing a value or modifying an object in the database
    ///
    /// @param path     the path to run the transaction on
    /// @param clazz    the class of the object to return
    /// @param function the function to apply to the current value of the data
    /// @param callback the callback to call when the operation is completed
    /// @see DatabaseReference#runTransaction(Transaction.Handler)
    private <T> void runTransaction(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull UnaryOperator<T> function, @NotNull final DatabaseCallback<T> callback) {
        readData(path).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                T currentValue = currentData.getValue(clazz);
                if (currentValue == null) {
                    currentValue = function.apply(null);
                } else {
                    currentValue = function.apply(currentValue);
                }
                currentData.setValue(currentValue);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (error != null) {
                    Log.e(TAG, "Transaction failed", error.toException());
                    callback.onFailed(error.toException());
                    return;
                }
                T result = currentData != null ? currentData.getValue(clazz) : null;
                callback.onCompleted(result);
            }
        });

    }

    // endregion of private methods for reading and writing data

    // public methods to interact with the database

    // region User Section


    /// create a new user in the database
    ///
    /// @param user     the user object to create (without the id, null)
    /// @param callback the callback to call when the operation is completed
    ///                              the callback will receive new user id
    ///                            if the operation fails, the callback will receive an exception
    /// @see DatabaseCallback
    /// @see User
    public void createNewUser(@NotNull final User user,
                              @Nullable final DatabaseCallback<String> callback) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "createUserWithEmail:success");
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        user.setId(uid);
                        writeData(USERS_PATH + "/" + uid, user, new DatabaseCallback<Void>() {
                            @Override
                            public void onCompleted(Void v) {
                                if (callback != null) callback.onCompleted(uid);
                            }

                            @Override
                            public void onFailed(Exception e) {
                                if (callback != null) callback.onFailed(e);
                            }
                        });
                    } else {
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        if (callback != null)
                            callback.onFailed(task.getException());
                    }
                });
    }


    /// Login with email and password
    ///
    /// @param email    , password
    /// @param callback the callback to call when the operation is completed
    ///                              the callback will receive String (user id)
    ///                            if the operation fails, the callback will receive an exception
    /// @see DatabaseCallback
    /// @see FirebaseAuth

    public void LoginUser(@NotNull final String email, final String password,
                          @Nullable final DatabaseCallback<String> callback) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)

                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Log.d("TAG", "createUserWithEmail:success");

                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        callback.onCompleted(uid);

                    } else {
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());

                        if (callback != null)
                            callback.onFailed(task.getException());
                    }
                });
    }


    /// get a user from the database
    ///
    /// @param uid      the id of the user to get
    /// @param callback the callback to call when the operation is completed
    ///                               the callback will receive the user object
    ///                             if the operation fails, the callback will receive an exception
    /// @see DatabaseCallback
    /// @see User
    public void getUser(@NotNull final String uid, @NotNull final DatabaseCallback<User> callback) {
        getData(USERS_PATH + "/" + uid, User.class, callback);
    }

    /// get all the users from the database
    ///
    /// @param callback the callback to call when the operation is completed
    ///                              the callback will receive a list of user objects
    ///                            if the operation fails, the callback will receive an exception
    /// @see DatabaseCallback
    /// @see List
    /// @see User
    public void getUserList(@NotNull final DatabaseCallback<List<User>> callback) {
        getDataList(USERS_PATH, User.class, callback);
    }

    /// delete a user from the database
    ///
    /// @param uid      the user id to delete
    /// @param callback the callback to call when the operation is completed
    public void deleteUser(@NotNull final String uid, @Nullable final DatabaseCallback<Void> callback) {
        deleteData(USERS_PATH + "/" + uid, callback);
    }


    public void updateUser(@NotNull final User user, @Nullable final DatabaseCallback<Void> callback) {
        runTransaction(USERS_PATH + "/" + user.getId(), User.class, currentUser -> user, new DatabaseCallback<User>() {
            @Override
            public void onCompleted(User object) {
                if (callback != null) {
                    callback.onCompleted(null);
                }
            }

            @Override
            public void onFailed(Exception e) {
                if (callback != null) {
                    callback.onFailed(e);
                }
            }
        });
    }


    // endregion User Section

    // region instructor section

    /// create a new instructor in the database
    ///
    /// @param instructor the instructor object to create
    /// @param callback   the callback to call when the operation is completed
    ///                                the callback will receive void
    ///                               if the operation fails, the callback will receive an exception
    /// @see DatabaseCallback
    /// @see Instructor
    public void createNewInstructor(@NotNull final Instructor instructor, @Nullable final DatabaseCallback<Void> callback) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(instructor.getEmail(), instructor.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "createUserWithEmail:success");
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        instructor.setId(uid);

                        writeData(INSTRUCTORS_PATH + "/" + instructor.getId(), instructor, callback);
                    } else {
                        Log.w("TAG", "createInstractorWithEmail:failure", task.getException());
                        if (callback != null)
                            callback.onFailed(task.getException());
                    }
                });
    }


    /// get a instructor from the database
    ///
    /// @param instructorId the id of the instructor to get
    /// @param callback     the callback to call when the operation is completed
    ///                                   the callback will receive the instructor object
    ///                                  if the operation fails, the callback will receive an exception
    /// @see DatabaseCallback
    /// @see Instructor
    public void getInstructor(@NotNull final String instructorId, @NotNull final DatabaseCallback<Instructor> callback) {
        getData(INSTRUCTORS_PATH + "/" + instructorId, Instructor.class, callback);
    }

    /// get all the instructors from the database
    ///
    /// @param callback the callback to call when the operation is completed
    ///                              the callback will receive a list of instructor objects
    ///                            if the operation fails, the callback will receive an exception
    /// @see DatabaseCallback
    /// @see List
    /// @see Instructor
    public void getInstructorList(@NotNull final DatabaseCallback<List<Instructor>> callback) {
        getDataList(INSTRUCTORS_PATH, Instructor.class, callback);
    }

    /// generate a new id for a new instructor in the database
    ///
    /// @return a new id for the instructor
    /// @see #generateNewId(String)
    /// @see Instructor
    public String generateInstructorId() {
        return generateNewId(INSTRUCTORS_PATH);
    }

    /// delete a instructor from the database
    ///
    /// @param instructorId the id of the instructor to delete
    /// @param callback     the callback to call when the operation is completed
    public void deleteInstructor(@NotNull final String instructorId, @Nullable final DatabaseCallback<Void> callback) {
        deleteData(INSTRUCTORS_PATH + "/" + instructorId, callback);
    }

    // endregion instructor section

    // region lesson section

    /// create a new lesson in the database
    /// @param lesson the lesson object to create
    /// @param callback the callback to call when the operation is completed
    ///               the callback will receive void
    ///              if the operation fails, the callback will receive an exception
    /// @see DatabaseCallback
    /// @see Lesson
//    public void createNewLesson(@NotNull final Lesson lesson, @Nullable final DatabaseCallback<Void> callback) {
//        writeData(LESSONS_PATH + "/" + lesson.getId(), lesson, callback);
//    }

    /// get a lesson from the database
    /// @param lessonId the id of the lesson to get
    /// @param callback the callback to call when the operation is completed
    ///                the callback will receive the lesson object
    ///               if the operation fails, the callback will receive an exception
    /// @see DatabaseCallback
    /// @see Lesson
//    public void getLesson(@NotNull final String lessonId, @NotNull final DatabaseCallback<Lesson> callback) {
//        getData(LESSONS_PATH + "/" + lessonId, Lesson.class, callback);
//    }

    /// get all the lessons from the database
    /// @param callback the callback to call when the operation is completed
    ///               the callback will receive a list of lesson objects
    ///
//    public void getLessonList(@NotNull final DatabaseCallback<List<Lesson>> callback) {
//        getDataList(LESSONS_PATH, Lesson.class, callback);
//    }

    /// get all the lessons of a specific user from the database
    /// @param uid the id of the user to get the lessons for
    /// @param callback the callback to call when the operation is completed
//    public void getUserLessonList(@NotNull String uid, @NotNull final DatabaseCallback<List<Lesson>> callback) {
//        getLessonList(new DatabaseCallback<>() {
//            @Override
//            public void onCompleted(List<Lesson> lessons) {
//                lessons.removeIf(lesson -> !Objects.equals(lesson.getUid(), uid));
//                callback.onCompleted(lessons);
//            }
//
//            @Override
//            public void onFailed(Exception e) {
//                callback.onFailed(e);
//            }
//        });
//    }


    /// generate a new id for a new lesson in the database
    ///
    /// @return a new id for the lesson
    /// @see #generateNewId(String)

    public String generateLessonId() {
        return generateNewId(LESSON_PATH);
    }

    /// delete a lesson from the database
    ///
    /// @param lessonId the id of the lesson to delete
    /// @param callback the callback to call when the operation is completed
    public void deleteLesson(@NotNull final String lessonId, @Nullable final DatabaseCallback<Void> callback) {
        deleteData(LESSON_PATH + "/" + lessonId, callback);
    }

    // endregion lesson section

}

