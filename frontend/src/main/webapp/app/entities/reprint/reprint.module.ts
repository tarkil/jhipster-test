import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FrontendSharedModule } from '../../shared';

import {
    ReprintService,
    ReprintPopupService,
    ReprintComponent,
    ReprintDetailComponent,
    ReprintDialogComponent,
    ReprintPopupComponent,
    ReprintDeletePopupComponent,
    ReprintDeleteDialogComponent,
    reprintRoute,
    reprintPopupRoute,
    ReprintResolvePagingParams,
} from './';

let ENTITY_STATES = [
    ...reprintRoute,
    ...reprintPopupRoute,
];

@NgModule({
    imports: [
        FrontendSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ReprintComponent,
        ReprintDetailComponent,
        ReprintDialogComponent,
        ReprintDeleteDialogComponent,
        ReprintPopupComponent,
        ReprintDeletePopupComponent,
    ],
    entryComponents: [
        ReprintComponent,
        ReprintDialogComponent,
        ReprintPopupComponent,
        ReprintDeleteDialogComponent,
        ReprintDeletePopupComponent,
    ],
    providers: [
        ReprintService,
        ReprintPopupService,
        ReprintResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FrontendReprintModule {}
