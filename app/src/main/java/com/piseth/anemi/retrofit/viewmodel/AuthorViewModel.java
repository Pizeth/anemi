package com.piseth.anemi.retrofit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.piseth.anemi.retrofit.repo.AuthorRetrofitRepo;
import com.piseth.anemi.utils.model.Author;

import java.util.List;

public class AuthorViewModel extends AndroidViewModel {
    private AuthorRetrofitRepo authorRetrofitRepo;
    private LiveData<List<Author>> allAuthors;

    public AuthorViewModel(@NonNull Application application) {
        super(application);
        authorRetrofitRepo = new AuthorRetrofitRepo();
        allAuthors = authorRetrofitRepo.getAll();
    }

    public LiveData<List<Author>> getAll() {
        return allAuthors;
    }
    public LiveData<Author> getById(long id) { return authorRetrofitRepo.getById(id); }
    public void add(Author author) { authorRetrofitRepo.add(author); }
    public void update(long id, Author author) { authorRetrofitRepo.update(id, author); }
    public void delete(long id) { authorRetrofitRepo.delete(id); }
}
