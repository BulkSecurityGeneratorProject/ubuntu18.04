/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DirectoryComponentsPage, DirectoryDeleteDialog, DirectoryUpdatePage } from './directory.page-object';

const expect = chai.expect;

describe('Directory e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let directoryUpdatePage: DirectoryUpdatePage;
    let directoryComponentsPage: DirectoryComponentsPage;
    let directoryDeleteDialog: DirectoryDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Directories', async () => {
        await navBarPage.goToEntity('directory');
        directoryComponentsPage = new DirectoryComponentsPage();
        await browser.wait(ec.visibilityOf(directoryComponentsPage.title), 5000);
        expect(await directoryComponentsPage.getTitle()).to.eq('Directories');
    });

    it('should load create Directory page', async () => {
        await directoryComponentsPage.clickOnCreateButton();
        directoryUpdatePage = new DirectoryUpdatePage();
        expect(await directoryUpdatePage.getPageTitle()).to.eq('Create or edit a Directory');
        await directoryUpdatePage.cancel();
    });

    it('should create and save Directories', async () => {
        const nbButtonsBeforeCreate = await directoryComponentsPage.countDeleteButtons();

        await directoryComponentsPage.clickOnCreateButton();
        await promise.all([
            directoryUpdatePage.setNameInput('name'),
            directoryUpdatePage.setParentInput('parent'),
            directoryUpdatePage.setTypeInput('type'),
            directoryUpdatePage.setTimeStampInput('01/01/2001' + protractor.Key.TAB + '02:30AM')
        ]);
        expect(await directoryUpdatePage.getNameInput()).to.eq('name');
        expect(await directoryUpdatePage.getParentInput()).to.eq('parent');
        expect(await directoryUpdatePage.getTypeInput()).to.eq('type');
        const selectedIsDirectory = directoryUpdatePage.getIsDirectoryInput();
        if (await selectedIsDirectory.isSelected()) {
            await directoryUpdatePage.getIsDirectoryInput().click();
            expect(await directoryUpdatePage.getIsDirectoryInput().isSelected()).to.be.false;
        } else {
            await directoryUpdatePage.getIsDirectoryInput().click();
            expect(await directoryUpdatePage.getIsDirectoryInput().isSelected()).to.be.true;
        }
        expect(await directoryUpdatePage.getTimeStampInput()).to.contain('2001-01-01T02:30');
        await directoryUpdatePage.save();
        expect(await directoryUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await directoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Directory', async () => {
        const nbButtonsBeforeDelete = await directoryComponentsPage.countDeleteButtons();
        await directoryComponentsPage.clickOnLastDeleteButton();

        directoryDeleteDialog = new DirectoryDeleteDialog();
        expect(await directoryDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Directory?');
        await directoryDeleteDialog.clickOnConfirmButton();

        expect(await directoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
