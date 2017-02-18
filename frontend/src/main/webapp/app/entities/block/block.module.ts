import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FrontendSharedModule } from '../../shared';

import {
    BlockService,
    BlockPopupService,
    BlockComponent,
    BlockDetailComponent,
    BlockDialogComponent,
    BlockPopupComponent,
    BlockDeletePopupComponent,
    BlockDeleteDialogComponent,
    blockRoute,
    blockPopupRoute,
    BlockResolvePagingParams,
} from './';

let ENTITY_STATES = [
    ...blockRoute,
    ...blockPopupRoute,
];

@NgModule({
    imports: [
        FrontendSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        BlockComponent,
        BlockDetailComponent,
        BlockDialogComponent,
        BlockDeleteDialogComponent,
        BlockPopupComponent,
        BlockDeletePopupComponent,
    ],
    entryComponents: [
        BlockComponent,
        BlockDialogComponent,
        BlockPopupComponent,
        BlockDeleteDialogComponent,
        BlockDeletePopupComponent,
    ],
    providers: [
        BlockService,
        BlockPopupService,
        BlockResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FrontendBlockModule {}
