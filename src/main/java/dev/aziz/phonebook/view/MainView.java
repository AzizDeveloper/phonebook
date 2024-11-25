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
import dev.aziz.phonebook.entity.User;
import dev.aziz.phonebook.service.UserService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Route("")
public class MainView extends AppLayout {

    private final UserService userService;
    private final Grid<User> grid;

    public MainView(UserService userService) {
        this.userService = userService;

        H1 title = new H1("Phone Book");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "var(--lumo-space-m)");

        Button createButton = new Button("Create User", e -> openUserDialog(new User(), true));

        Anchor exportLink = new Anchor();
        exportLink.setText("Export Data (PDF)");
        exportLink.getElement().setAttribute("download", true);

        updateExportLink(exportLink);

        HorizontalLayout buttonLayout = new HorizontalLayout(createButton, exportLink);
        buttonLayout.setSpacing(true); // Add spacing between components
        buttonLayout.getStyle().set("margin-left", "auto");
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout header = new HorizontalLayout(title, buttonLayout);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        addToNavbar(header);

        grid = new Grid<>(User.class, false);
        grid.addColumn(User::getFirstName).setHeader("First Name");
        grid.addColumn(User::getLastName).setHeader("Last Name");
        grid.addColumn(User::getEmail).setHeader("Email");
        grid.addColumn(User::getPhoneNumber).setHeader("Phone Number");
        grid.addComponentColumn(this::createActionButtons).setHeader("Actions");

        refreshGrid();
        grid.setAllRowsVisible(true);
        setContent(grid);
    }
    private void updateExportLink(Anchor exportLink) {
        StreamResource resource = new StreamResource("phonebook-export.pdf", () -> {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            try {
                PdfWriter writer = new PdfWriter(byteArrayOutputStream);
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument);

                document.add(new Paragraph("Phone Book Export").setTextAlignment(TextAlignment.CENTER).setBold());

                Table table = new Table(4);
                table.addHeaderCell("First Name");
                table.addHeaderCell("Last Name");
                table.addHeaderCell("Email");
                table.addHeaderCell("Phone Number");

                List<User> users = userService.getAllUsers();
                for (User user : users) {
                    table.addCell(user.getFirstName());
                    table.addCell(user.getLastName());
                    table.addCell(user.getEmail());
                    table.addCell(user.getPhoneNumber());
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

    private HorizontalLayout createActionButtons(User user) {
        Button editButton = new Button("Edit", e -> openUserDialog(user, false));
        Button deleteButton = new Button("Delete", e -> {
            userService.deleteUserById(user.getId());
            refreshGrid();
        });

        return new HorizontalLayout(editButton, deleteButton);
    }

    private void refreshGrid() {
        List<User> people = userService.getAllUsers();
        grid.setItems(people);
    }

    private void openUserDialog(User user, boolean isNew) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(isNew ? "Create User" : "Edit User");

        TextField firstNameField = new TextField("First Name");
        firstNameField.setValue(user.getFirstName() != null ? user.getFirstName() : "");

        TextField lastNameField = new TextField("Last Name");
        lastNameField.setValue(user.getLastName() != null ? user.getLastName() : "");

        TextField emailField = new TextField("Email");
        emailField.setValue(user.getEmail() != null ? user.getEmail() : "");

        TextField phoneNumberField = new TextField("Phone Number");
        phoneNumberField.setValue(user.getPhoneNumber() != null ? user.getPhoneNumber().toString() : "");
        phoneNumberField.setPattern("\\d*");


        Button saveButton = new Button("Save", e -> {
            user.setFirstName(firstNameField.getValue());
            user.setLastName(lastNameField.getValue());
            user.setEmail(emailField.getValue());
            user.setPhoneNumber(phoneNumberField.getValue());

            if (isNew) {
                userService.createUser(user);
            } else {
                userService.updateUser(user.getId(), user);
            }

            refreshGrid();
            dialog.close();
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());

        VerticalLayout dialogLayout = new VerticalLayout(
                firstNameField, lastNameField, emailField, phoneNumberField
        );
        dialog.add(dialogLayout);
        dialog.getFooter().add(saveButton, cancelButton);
        dialog.open();
    }
}



