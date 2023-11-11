package com.piseth.anemi.retrofit.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.piseth.anemi.retrofit.apiservices.UserCallBack;
import com.piseth.anemi.retrofit.repo.BookRetrofitRepo;
import com.piseth.anemi.retrofit.repo.UserRetrofitRepo;
import com.piseth.anemi.utils.model.Book;
import com.piseth.anemi.utils.model.TokenUser;
import com.piseth.anemi.utils.model.User;

import java.util.List;

public class BookViewModel extends AndroidViewModel {
    private BookRetrofitRepo bookRetrofitRepo;
    private LiveData<List<Book>> allBooks;

    public BookViewModel(@NonNull Application application) {
        super(application);
        bookRetrofitRepo = new BookRetrofitRepo();
        allBooks = bookRetrofitRepo.getAllBooks();
    }

    public LiveData<List<Book>> getAllBooks() {
        return allBooks;
    }
    public LiveData<Book> getBookById(long id) { return bookRetrofitRepo.getBookById(id); }
    public void addBook(Book book) { bookRetrofitRepo.addBook(book); }
    public void updateBook(long id, Book book) { bookRetrofitRepo.updateBook(id, book); }
    public void deleteBook(long id) { bookRetrofitRepo.deleteBook(id); }
}
