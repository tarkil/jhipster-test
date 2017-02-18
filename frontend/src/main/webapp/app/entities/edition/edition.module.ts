import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FrontendSharedModule } from '../../shared';

import {
    EditionService,
    EditionPopupService,
    EditionComponent,
    EditionDetailComponent,
    EditionDialogComponent,
    EditionPopupComponent,
    EditionDeletePopupComponent,
    EditionDeleteDialogComponent,
    editionRoute,
    editionPopupRoute,
    EditionResolvePagingParams,
} from './';

let ENTITY_STATES = [
    ...editionRoute,
    ...editionPopupRoute,
];

@NgModule({
    imports: [
        FrontendSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        EditionComponent,
        EditionDetailComponent,
        EditionDialogComponent,
        EditionDeleteDialogComponent,
        EditionPopupComponent,
        EditionDeletePopupComponent,
    ],
    entryComponents: [
        EditionComponent,
        EditionDialogComponent,
        EditionPopupComponent,
        EditionDeleteDialogComponent,
        EditionDeletePopupComponent,
    ],
    providers: [
        EditionService,
        EditionPopupService,
        EditionResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FrontendEditionModule {}
