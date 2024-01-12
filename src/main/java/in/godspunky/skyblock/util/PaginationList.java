package in.godspunky.skyblock.util;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
public class PaginationList<T> extends ArrayList<T> {
    private int elementsPerPage;

    public PaginationList(final Collection<T> collection, final int elementsPerPage) {
        super(collection);
        this.elementsPerPage = elementsPerPage;
    }

    public PaginationList(final int elementsPerPage) {
        this.elementsPerPage = elementsPerPage;
    }

    @SafeVarargs
    public PaginationList(final int elementsPerPage, final T... elements) {
        super(Arrays.asList(elements));
        this.elementsPerPage = elementsPerPage;
    }

    public int getPageCount() {
        return (int) Math.ceil(this.size() / (double) this.elementsPerPage);
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
        Collections.addAll(this, t);
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
