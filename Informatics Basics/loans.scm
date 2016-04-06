(define (loans s p n)
  (let* ((r (/ p 100))
         (m (/ (* s r (expt (+ 1 r) n)) (* 12 (- (expt (+ 1 r) n) 1)))))
    (exact->inexact (/ (round (* m (expt 10 2))) (expt 10 2)))))
         