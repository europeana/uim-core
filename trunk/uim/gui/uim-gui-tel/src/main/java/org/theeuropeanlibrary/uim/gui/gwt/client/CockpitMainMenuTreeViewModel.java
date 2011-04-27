/* CockpitTreeViewModel.java - created on Apr 27, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.gui.gwt.client;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

/**
 * The {@link TreeViewModel} used by the main menu.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class CockpitMainMenuTreeViewModel implements TreeViewModel {
    /**
     * The top level categories.
     */
    private final ListDataProvider<Category> categories = new ListDataProvider<Category>();

    /**
     * Creates a new instance of this class.
     */
    public CockpitMainMenuTreeViewModel() {
        List<Category> catList = categories.getList();

        {
            Category category = new Category("Overview");
            catList.add(category);
        }

        {
            Category category = new Category("Monitor");
            catList.add(category);
        }
    }

    @Override
    public <T> NodeInfo<?> getNodeInfo(T value) {
        if (value == null) {
            // Return the top level categories.
            return new DefaultNodeInfo<Category>(categories, new CategoryCell());
        } else if (value instanceof Category) {
            // Return the examples within the category.
//            Category category = (Category)value;
            return null; //category.getNodeInfo();
        }
        return null;
    }

    @Override
    public boolean isLeaf(Object value) {
        return value != null && !(value instanceof Category);
    }

    /**
     * A top level category in the tree.
     */
    private static class Category {
        private final String name;

        public Category(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * The cell used to render categories.
     */
    private static class CategoryCell extends AbstractCell<Category> {
        @Override
        public void render(Context context, Category value, SafeHtmlBuilder sb) {
            if (value != null) {
                sb.appendEscaped(value.getName());
            }
        }
    }
}
