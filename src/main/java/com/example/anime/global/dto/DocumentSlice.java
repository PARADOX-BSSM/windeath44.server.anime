package com.example.anime.global.dto;

import com.example.anime.domain.anime.model.AnimeDocument;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public class DocumentSlice<T> implements Slice<T> {
    private List<T> values;
    private Boolean hasNext;

    @Override
    public int getNumber() {
        return 0;
    }

    @Override
    public int getSize() {
        return values.isEmpty() ? 0 : values.size();
    }

    @Override
    public int getNumberOfElements() {
        return 0;
    }

    @Override
    public List<T> getContent() {
        return values;
    }

    @Override
    public boolean hasContent() {
        return !values.isEmpty();
    }

    @Override
    public Sort getSort() {
        return null;
    }

    @Override
    public boolean isFirst() {
        return false;
    }

    @Override
    public boolean isLast() {
        return false;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public Pageable nextPageable() {
        return null;
    }

    @Override
    public Pageable previousPageable() {
        return null;
    }

    @Override
    public <U> Slice<U> map(Function<? super T, ? extends U> converter) {
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }
}
