import styles from './ProjectsForm.module.css'

export default function ProjectsPage() {
    return (
        <div className={styles['container']}>
            <div className={styles['calender-container']}>
                <nav className={styles['nav']}>
                    <h2 className={styles['title-bar']}>2025년 3월 프로젝트 일정</h2>
                    <div className={`${styles['button-group']} ${styles['align-right']}`}>
                        <button className={`${styles['button']} ${styles['small']}`}> &lt;️ </button>
                        <button className={`${styles['button']} ${styles['small']}`}>오늘</button>
                        <button className={`${styles['button']} ${styles['small']}`}> &gt;️ </button>
                    </div>
                </nav>

                <div className={styles['card-container']}>
                    <div>
                        <div className={styles['day-cell']}>
                            <div className={`${styles['day-cell']} ${styles['sunday']}`}>
                                <div className={styles['circle-hover']}>일</div>
                            </div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>월</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>화</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>수</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>목</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>금</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>토</div></div>
                        </div>

                        <div className={styles['day-group']}>
                            <div className={`${styles['day-cell']} ${styles['sunday']}`}>
                                <div className=""></div>
                            </div>
                            <div className={styles['day-cell']}><div className=""></div></div>
                            <div className={styles['day-cell']}><div className=""></div></div>
                            <div className={styles['day-cell']}><div className=""></div></div>
                            <div className={styles['day-cell']}><div className=""></div></div>
                            <div className={styles['day-cell']}><div className=""></div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>1</div></div>
                        </div>

                        <div className={styles['day-group']}>
                            <div className={`${styles['day-cell']} ${styles['sunday']}`}>
                                <div className={styles['circle-hover']}>2</div>
                            </div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>3</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>4</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>5</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>6</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>7</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>8</div></div>
                        </div>

                        <div className={styles['day-group']}>
                            <div className={`${styles['day-cell']} ${styles['sunday']}`}>
                                <div className={styles['circle-hover']}>
                                    9
                                    <div className={styles['dot']}></div>
                                </div>
                            </div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>10</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>11</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>12</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>13<div className={styles['dot']}></div></div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>14</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>15</div></div>
                        </div>

                        <div className={styles['day-group']}>
                            <div className={`${styles['day-cell']} ${styles['sunday']}`}>
                                <div className={styles['circle-hover']}>16</div>
                            </div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>17</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>18</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>19</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>20</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>21</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>22</div></div>
                        </div>

                        <div className={styles['day-group']}>
                            <div className={`${styles['day-cell']} ${styles['sunday']}`}>
                                <div className={styles['circle-hover']}>23</div>
                            </div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>24</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>25</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>26</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>27</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>28</div></div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>29</div></div>
                        </div>

                        <div className={styles['day-group']}>
                            <div className={`${styles['day-cell']} ${styles['sunday']}`}>
                                <div className={styles['circle-hover']}>30</div>
                            </div>
                            <div className={styles['day-cell']}><div className={styles['circle-hover']}>31</div></div>
                            <div className={styles['day-cell']}><div className=""></div></div>
                            <div className={styles['day-cell']}><div className=""></div></div>
                            <div className={styles['day-cell']}><div className=""></div></div>
                            <div className={styles['day-cell']}><div className=""></div></div>
                            <div className={styles['day-cell']}><div className=""></div></div>
                        </div>
                    </div>
                </div>
                <div className={styles['spacer']}></div>
            </div>
        </div>


    )
}
