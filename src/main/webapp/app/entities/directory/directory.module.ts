import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Ubuntu18SharedModule } from 'app/shared';
import {
    DirectoryComponent,
    DirectoryDetailComponent,
    DirectoryUpdateComponent,
    DirectoryDeletePopupComponent,
    DirectoryDeleteDialogComponent,
    directoryRoute,
    directoryPopupRoute
} from './';

const ENTITY_STATES = [...directoryRoute, ...directoryPopupRoute];

@NgModule({
    imports: [Ubuntu18SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DirectoryComponent,
        DirectoryDetailComponent,
        DirectoryUpdateComponent,
        DirectoryDeleteDialogComponent,
        DirectoryDeletePopupComponent
    ],
    entryComponents: [DirectoryComponent, DirectoryUpdateComponent, DirectoryDeleteDialogComponent, DirectoryDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Ubuntu18DirectoryModule {}
