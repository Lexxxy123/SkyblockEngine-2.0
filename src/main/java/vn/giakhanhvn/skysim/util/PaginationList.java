package vn.giakhanhvn.skysim.util;

import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;

public class PaginationList<T> extends ArrayList<T>
{
    private int elementsPerPage;
    
    public PaginationList(final Collection<T> collection, final int elementsPerPage) {
        super(collection);
        this.elementsPerPage = elementsPerPage;
    }
    
    public PaginationList(final int elementsPerPage) {
        this.elementsPerPage = elementsPerPage;
    }
    
    public PaginationList(final int elementsPerPage, final T... elements) {
        super(Arrays.<T>asList(elements));
        this.elementsPerPage = elementsPerPage;
    }
    
    public int getElementsPerPage() {
        return this.elementsPerPage;
    }
    
    public void setElementsPerPage(final int elementsPerPage) {
        this.elementsPerPage = elementsPerPage;
    }
    
    public int getPageCount() {
        return (int)Math.ceil(this.size() / (double)this.elementsPerPage);
    }
    
    public List<T> getPage(final int page) {
        if (page < 1 || page > this.getPageCount()) {
            return null;
        }
        final int startIndex = (page - 1) * this.elementsPerPage;
        final int endIndex = Math.min(startIndex + this.elementsPerPage, this.size());
        return this.subList(startIndex, endIndex);
    }
    
    public List<List<T>> getPages() {
        final List<List<T>> pages = new ArrayList<List<T>>();
        for (int i = 1; i <= this.getPageCount(); ++i) {
            pages.add(this.getPage(i));
        }
        return pages;
    }
    
    public void addAll(final T[] t) {
        for (final T o : t) {
            this.add(o);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder res = new StringBuilder();
        for (int i = 1; i <= this.getPageCount(); ++i) {
            res.append("Page ").append(i).append(": ").append("\n");
            for (final T element : this.getPage(i)) {
                res.append(" - ").append(element).append("\n");
            }
        }
        return res.toString();
    }
}
