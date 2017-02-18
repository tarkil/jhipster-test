import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Block } from './block.model';
import { BlockService } from './block.service';
@Injectable()
export class BlockPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private blockService: BlockService
    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.blockService.find(id).subscribe(block => {
                if (block.launchDate) {
                    block.launchDate = {
                        year: block.launchDate.getFullYear(),
                        month: block.launchDate.getMonth() + 1,
                        day: block.launchDate.getDate()
                    };
                }
                this.blockModalRef(component, block);
            });
        } else {
            return this.blockModalRef(component, new Block());
        }
    }

    blockModalRef(component: Component, block: Block): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.block = block;
        modalRef.result.then(result => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
