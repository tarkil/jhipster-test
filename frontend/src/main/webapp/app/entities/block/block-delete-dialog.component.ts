import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Block } from './block.model';
import { BlockPopupService } from './block-popup.service';
import { BlockService } from './block.service';

@Component({
    selector: 'jhi-block-delete-dialog',
    templateUrl: './block-delete-dialog.component.html'
})
export class BlockDeleteDialogComponent {

    block: Block;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private blockService: BlockService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['block']);
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.blockService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'blockListModification',
                content: 'Deleted an block'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-block-delete-popup',
    template: ''
})
export class BlockDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private blockPopupService: BlockPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.blockPopupService
                .open(BlockDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
