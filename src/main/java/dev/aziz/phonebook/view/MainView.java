package dev.aziz.phonebook.view;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import dev.aziz.phonebook.entity.Item;
import dev.aziz.phonebook.service.ItemService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Route("")
public class MainView extends AppLayout {

    private final ItemService itemService;
    private final Grid<Item> grid;
    private Item selectedItem;
    private List<Item> selectedItems = new ArrayList<>();
    private TextField filterByNameTextField;

    public MainView(ItemService itemService) {
        this.itemService = itemService;

        H1 title = new H1("Phone Book");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "var(--lumo-space-m)");

        Button createButton = new Button("Create Item", e -> openItemDialog(new Item(), true));
        Button openButton = new Button("Open");
        Button createdOrders = new Button("Created purchase orders");
        Button createOrder = new Button("Create order", e -> openCreateOrderDialog(selectedItems));

        grid = new Grid<>(Item.class, false);

        filterByNameTextField = new TextField("", "Filter by Name");
        filterByNameTextField.getStyle().set("margin-right", "auto");

        Button getItemsFilter = new Button("Filter", e -> refreshGrid());

        Anchor exportLink = new Anchor();
        exportLink.setText("Export Data (PDF)");
        exportLink.getElement().setAttribute("download", true);
        updateExportLink(exportLink);

        Button editButton = new Button("Edit", e -> {
            if (selectedItem != null) {
                openItemDialog(selectedItem, false);
            }
        });
        editButton.setEnabled(false);

        Button deleteButton = new Button("Delete", e -> {
            if (selectedItem != null) {
                itemService.deleteItemById(selectedItem.getId());
                refreshGrid();
                grid.deselectAll();
            }
        });
        deleteButton.setEnabled(false);

        HorizontalLayout buttonLayout = new HorizontalLayout(openButton, createButton, editButton, deleteButton, exportLink);
        buttonLayout.setSpacing(true);
        buttonLayout.getStyle().set("margin-left", "auto");
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout header = new HorizontalLayout(title, filterByNameTextField, getItemsFilter, buttonLayout);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout footer = new HorizontalLayout(createdOrders, createOrder);
        footer.getStyle().set("position", "sticky")
                .set("bottom", "0")
                .set("width", "100%")
                .set("padding", "10px");
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);


        grid.addComponentColumn(item -> {
            Checkbox checkbox = new Checkbox();
            checkbox.addValueChangeListener(event -> {
                if (event.getValue()) {
                    selectedItems.add(item);
                } else {
                    selectedItems.remove(item);
                }
            });
            return checkbox;
        }).setHeader("Select");

        grid.addColumn(Item::getName).setHeader("Name").setSortable(true);
        grid.addColumn(Item::getAmount).setHeader("Amount").setSortable(true);
        grid.addColumn(Item::getUnitOfMeasure).setHeader("Unit of Measurement").setSortable(true);
        grid.addColumn(Item::getPrice).setHeader("Price").setSortable(true);

        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedItem = event.getValue();
            boolean hasSelection = selectedItem != null;
            editButton.setEnabled(hasSelection);
            deleteButton.setEnabled(hasSelection);
        });

        refreshGrid();
        grid.setAllRowsVisible(true);
        grid.setHeight("calc(100vh - 200px)");

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.add(header);
        mainLayout.add(grid);
        mainLayout.add(footer);
        setContent(mainLayout);
        mainLayout.expand(grid);
        mainLayout.setFlexGrow(1, grid);
    }

    private void updateExportLink(Anchor exportLink) {
        StreamResource resource = new StreamResource("items-export.pdf", () -> {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            try {
                PdfWriter writer = new PdfWriter(byteArrayOutputStream);
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument);

                document.add(new Paragraph("Items Export").setTextAlignment(TextAlignment.CENTER).setBold());

                Table table = new Table(4);
                table.addHeaderCell("Name");
                table.addHeaderCell("Amount");
                table.addHeaderCell("Unit of Measurement");
                table.addHeaderCell("Price");

                List<Item> items = itemService.getAllItems();
                for (Item item : items) {
                    table.addCell(item.getName());
                    table.addCell(item.getAmount().toString());
                    table.addCell(item.getUnitOfMeasure());
                    table.addCell(item.getPrice().toString());
                }

                document.add(table);
                document.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        });

        exportLink.setHref(resource);
    }

    private void refreshGrid() {
        if (!filterByNameTextField.getValue().isEmpty()) {
            System.out.println("Get items by Filter");
            System.out.println("filterByNameTextField = " + filterByNameTextField.getValue());
            List<Item> items = itemService.getItemsByFilter(filterByNameTextField.getValue());
            System.out.println("items size = " + items.size());
            grid.setItems(items);
        } else {
            System.out.println("Get just all items");
            List<Item> items = itemService.getAllItems();
            System.out.println("items size = " + items.size());
            grid.setItems(items);
        }
    }

    private void openItemDialog(Item item, boolean isNew) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(isNew ? "Create Item" : "Edit Item");

        TextField nameField = new TextField("Name");
        nameField.setValue(item.getName() != null ? item.getName() : "");

        TextField amountField = new TextField("Amount");
        amountField.setValue(item.getAmount() != null ? item.getAmount().toString() : "");

        TextField unitOfMeasurementField = new TextField("Unit of Measurement");
        unitOfMeasurementField.setValue(item.getUnitOfMeasure() != null ? item.getUnitOfMeasure() : "");

        TextField priceField = new TextField("Price");
        priceField.setValue(item.getPrice() != null ? String.valueOf(item.getPrice()) : "");

        Button saveButton = new Button("Save", e -> {
            item.setName(nameField.getValue());
            item.setAmount(Integer.valueOf(amountField.getValue()));
            item.setUnitOfMeasure(unitOfMeasurementField.getValue());
            item.setPrice(new BigDecimal(priceField.getValue()));

            if (isNew) {
                itemService.createItem(item);
            } else {
                itemService.updateItem(item.getId(), item);
            }

            refreshGrid();
            dialog.close();
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());

        VerticalLayout dialogLayout = new VerticalLayout(
                nameField, amountField, unitOfMeasurementField, priceField
        );
        dialog.add(dialogLayout);
        dialog.getFooter().add(saveButton, cancelButton);
        dialog.open();
    }

    private void openCreateOrderDialog(List<Item> items) {
        if (!items.isEmpty()) {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("Create Order");

            VerticalLayout dialogLayout = new VerticalLayout();
            dialogLayout.setSizeFull();
            dialogLayout.setPadding(false);
            dialogLayout.setSpacing(false);

            final Grid<Item> selectedItemsGrid = new Grid<>(Item.class, false);
            selectedItemsGrid.addColumn(Item::getName).setHeader("Name").setAutoWidth(true);
            selectedItemsGrid.addColumn(Item::getAmount).setHeader("Amount").setAutoWidth(true);
            selectedItemsGrid.addColumn(Item::getUnitOfMeasure).setHeader("Unit of Measurement").setAutoWidth(true);
            selectedItemsGrid.addColumn(Item::getPrice).setHeader("Price").setAutoWidth(true);
            selectedItemsGrid.addColumn(item -> item.getAmount() * item.getPrice().doubleValue())
                    .setHeader("Total Price")
                    .setAutoWidth(true)
                    .setFlexGrow(1);
            selectedItemsGrid.setItems(items);
            selectedItemsGrid.setWidthFull();
            selectedItemsGrid.setAllRowsVisible(true);
            selectedItemsGrid.setHeight("300px");

            dialogLayout.add(selectedItemsGrid);

            Button cancelButton = new Button("Cancel", e -> dialog.close());
            Button saveButton = new Button("Save", e -> dialog.close());
            Button chooseSupplierButton = new Button("Choose Supplier", e -> dialog.close());
            chooseSupplierButton.getStyle().set("margin-right", "auto");

            dialog.add(dialogLayout);
            dialog.getFooter().add(chooseSupplierButton, cancelButton, saveButton);
            dialog.open();
        } else {
            Notification.show("No items selected for the order.", 3000, Notification.Position.MIDDLE);
        }
    }
}



