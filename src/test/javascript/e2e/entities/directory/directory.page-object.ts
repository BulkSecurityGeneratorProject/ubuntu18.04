import { element, by, ElementFinder } from 'protractor';

export class DirectoryComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-directory div table .btn-danger'));
    title = element.all(by.css('jhi-directory div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getText();
    }
}

export class DirectoryUpdatePage {
    pageTitle = element(by.id('jhi-directory-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    parentInput = element(by.id('field_parent'));
    typeInput = element(by.id('field_type'));
    isDirectoryInput = element(by.id('field_isDirectory'));
    timeStampInput = element(by.id('field_timeStamp'));

    async getPageTitle() {
        return this.pageTitle.getText();
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    async setParentInput(parent) {
        await this.parentInput.sendKeys(parent);
    }

    async getParentInput() {
        return this.parentInput.getAttribute('value');
    }

    async setTypeInput(type) {
        await this.typeInput.sendKeys(type);
    }

    async getTypeInput() {
        return this.typeInput.getAttribute('value');
    }

    getIsDirectoryInput() {
        return this.isDirectoryInput;
    }
    async setTimeStampInput(timeStamp) {
        await this.timeStampInput.sendKeys(timeStamp);
    }

    async getTimeStampInput() {
        return this.timeStampInput.getAttribute('value');
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class DirectoryDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-directory-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-directory'));

    async getDialogTitle() {
        return this.dialogTitle.getText();
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
