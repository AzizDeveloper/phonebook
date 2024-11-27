package dev.aziz.phonebook.view;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import dev.aziz.phonebook.entity.Item;
import dev.aziz.phonebook.service.ItemService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

@Route("")
public class MainView extends AppLayout {

    private final ItemService itemService;
    private final Grid<Item> grid;
    private Item selectedItem;

    public MainView(ItemService itemService) {
        this.itemService = itemService;

        H1 title = new H1("Phone Book");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "var(--lumo-space-m)");

        Button createButton = new Button("Create User", e -> openItemDialog(new Item(), true));
        Button openButton = new Button("Open");
        Button createdOrders = new Button("Created purchase orders");
        Button createOrder = new Button("Create order");

        grid = new Grid<>(Item.class, false);

        TextField filterByNameTextField = new TextField("", "Filter by Name");
        filterByNameTextField.getStyle().set("margin-right", "auto");

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

        HorizontalLayout header = new HorizontalLayout(title, filterByNameTextField, buttonLayout);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout footer = new HorizontalLayout(createdOrders, createOrder);
        footer.getStyle().set("position", "sticky")
                .set("bottom", "0")
                .set("width", "100%")
                .set("padding", "10px");
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        grid.addColumn(Item::getName).setHeader("Name");
        grid.addColumn(Item::getAmount).setHeader("Amount");
        grid.addColumn(Item::getUnitOfMeasure).setHeader("Unit of Measurement");
        grid.addColumn(Item::getPrice).setHeader("Price");

        // Add selection listener
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

//    private HorizontalLayout createActionButtons(User user) {
//        Button editButton = new Button("Edit", e -> openUserDialog(user, false));
//        Button deleteButton = new Button("Delete", e -> {
//            userService.deleteUserById(user.getId());
//            refreshGrid();
//        });
//
//        return new HorizontalLayout(editButton, deleteButton);
//    }

    private void refreshGrid() {
        List<Item> items = itemService.getAllItems();
        grid.setItems(items);
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
}



